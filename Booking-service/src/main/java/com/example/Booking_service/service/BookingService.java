package com.example.Booking_service.service;

import com.example.Booking_service.dto.BookingCreatedEvent;
import com.example.Booking_service.dto.CreateBookingRequest;
import com.example.Booking_service.model.Booking;
import com.example.Booking_service.model.Status;
import com.example.Booking_service.model.WorkplaceInventory;
import com.example.Booking_service.repository.BookingRepository;
import com.example.Booking_service.repository.WorkplaceInventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private WorkplaceInventoryRepository workplaceInventoryRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String USER_BOOKING_TOPIC = "user-booking-topic";

    // бронируем место
    @Transactional
    public Booking createBooking(CreateBookingRequest request){

        // получаем userId из jwt токена
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String employeeId = jwt.getClaimAsString("employeeId");

        WorkplaceInventory inventory = workplaceInventoryRepository.findByWorkplaceId(request.getWorkplaceId())
                .orElseThrow(() -> new RuntimeException("Данное рабочее место не найдено в инвентаре."));

        // ПРОВЕРКА СВОБОДЕН ЛИ СЛОТ В БД
        if(inventory.getStatus() == Status.Booked){
            throw new RuntimeException("Место с номером: " + request.getWorkplaceId() + " уже занято");
        }

        if (!request.getEndBooking().isAfter(request.getStartBooking())) {
            throw new RuntimeException("Время окончания должно быть позже времени начала");
        }

        if (request.getDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Нельзя бронировать на прошедшую дату");
        }

        if (request.getDate().isEqual(LocalDate.now()) &&
                request.getStartBooking().isBefore(LocalTime.now())) {
            throw new RuntimeException("Нельзя бронировать на прошедшее время");
        }

        boolean conflict = bookingRepository.isTimeSlotBooked(
                request.getWorkplaceId(),
                request.getDate(),
                request.getStartBooking(),
                request.getEndBooking()
        );
        if(conflict){
            throw new RuntimeException("Место " + request.getWorkplaceId() + " уже занято в указанный временной интервал и дату.");
        }

        // бронируем место
        Booking booking = new Booking();

        // переносим данные из инвентаря в транзакцию бронирования
        booking.setWorkplaceId(inventory.getWorkplaceId());
        booking.setFloor(inventory.getFloor());

        // заполняем остальные поля бронирования
        booking.setEmployeeId(employeeId);
        booking.setStatus(Status.Booked);
        booking.setDate(request.getDate());
        booking.setStartBooking(request.getStartBooking());
        booking.setEndBooking(request.getEndBooking());


        // Сохраняем новую запись в таблице Booking
        Booking bookingSaved = bookingRepository.save(booking);

        BookingCreatedEvent event = new BookingCreatedEvent(
                bookingSaved.getEmployeeId(), bookingSaved.getWorkplaceId()
        );


        try{
            kafkaTemplate.send(USER_BOOKING_TOPIC, bookingSaved.getEmployeeId(),event);
            System.out.println("Забронирвоано новое место:" + bookingSaved.getWorkplaceId() + "\nПользователь: " + bookingSaved.getEmployeeId());
        } catch (Exception e){
            System.err.println("Ошибка, сообшение не отправлено в Kafka, откат транзакции " + e.getMessage());
            throw new RuntimeException("Не удалось отправить сообщение в Kafka" + e.getMessage());
        }

        return bookingSaved;

    }

    // отмена брони -
    @Transactional
    public void cancellationBooking(String workplaceId){

        // получаем userId из jwt токена
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String employeeId = jwt.getClaimAsString("employeeId");

        Booking booking = bookingRepository.findByWorkplaceId(workplaceId).orElseThrow(
                () -> new RuntimeException("Брони не существует!"));

        if(!booking.getEmployeeId().equals(employeeId)){
             throw new RuntimeException("Вы не можете отменить чужую бронь!");
        }

        bookingRepository.delete(booking);

    }

    // изменить бронь -
    @Transactional
    public Booking extendBooking(CreateBookingRequest request){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String employeeId = jwt.getClaimAsString("employeeId");

        Booking booking = bookingRepository.findByWorkplaceId(request.getWorkplaceId()).orElseThrow(
                () -> new RuntimeException("Брони не существует!"));

        if(!booking.getEmployeeId().equals(employeeId)){
            throw new RuntimeException("Вы не можете изменить чужую бронь!");
        }

        // изменяем параметры брони
        booking.setDate(request.getDate());
        booking.setStartBooking(request.getStartBooking());
        booking.setEndBooking(request.getEndBooking());

        return bookingRepository.save(booking);
    }
}
