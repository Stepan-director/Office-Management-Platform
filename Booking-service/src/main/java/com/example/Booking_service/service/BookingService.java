package com.example.Booking_service.service;

import com.example.Booking_service.dto.BookingCreatedEvent;
import com.example.Booking_service.dto.CreateBookingRequest;
import com.example.Booking_service.model.Booking;
import com.example.Booking_service.model.Status;
import com.example.Booking_service.repository.BookingRepository;
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
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String USER_BOOKING_TOPIC = "user-booking-topic";

    // бронируем место
    @Transactional
    public Booking createBooking(CreateBookingRequest request){

        // получаем userId из jwt токена
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String employeeId = jwt.getClaimAsString("employeeId");

        // проверка свободно ли место
        if(request.getStatus() == Status.Booked){
            throw new RuntimeException("Место с номером: " + request.getWorkplaceId() + " занят");
        }

        // проврека существует ли это место на самом деле или ошибка на фронте
        if(!bookingRepository.existsByWorkplaceId(request.getWorkplaceId())){
            throw new RuntimeException("Данное место не существует. Приносим извинения!");
        }

        // бронируем место
        Booking booking = new Booking();
        booking.setEmployeeId(employeeId);
        booking.setWorkplaceId(request.getWorkplaceId());
        booking.setFloor(request.getFloor());
        booking.setStatus(Status.Booked);
        booking.setDate(LocalDate.now());
        booking.setStartBooking(LocalTime.now());
        booking.setEndBooking(request.getEndBooking());

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

    // отмена брони
    @Transactional
    public Booking cancellationBooking(CreateBookingRequest request){
        // проверка занято ли место перед тем как его освободить
        if(request.getStatus() == Status.Free){
            throw new RuntimeException("Место с номеров " + request.getWorkplaceId() + " свободен.");
        }

        // проверка сущетсвует ли место на самом деле
        if(!bookingRepository.existsByWorkplaceId(request.getWorkplaceId())){
            throw new RuntimeException("Данное место не существует. Приносим извинения!");
        }

        Booking booking = bookingRepository.findByWorkplaceIdAndEmployeeIdAndStatus(request.getWorkplaceId(), request.getEmployeeId(), Status.Booked)
                .orElseThrow(() -> new RuntimeException("Активная бронь на место "
                        + request.getWorkplaceId() + " не найдена"));

        booking.setStatus(Status.Free);
        booking.setDate(null);
        booking.setStartBooking(null);
        booking.setEndBooking(null);

        return bookingRepository.save(booking);

    }

    // продлить бронь
    @Transactional
    public Booking extendBooking(CreateBookingRequest request){

        // проверка занято ли место перед тем как его продлить
        if(request.getStatus() == Status.Free){
            throw new RuntimeException("Место с номеров " + request.getWorkplaceId() + " свободен. Продление невозможно");
        }

        // проверка сущетсвует ли место на самом деле
        if(!bookingRepository.existsByWorkplaceId(request.getWorkplaceId())){
            throw new RuntimeException("Данное место не существует. Приносим извинения!");
        }

        Booking booking = bookingRepository.findByWorkplaceIdAndEmployeeIdAndStatus(request.getWorkplaceId(), request.getEmployeeId(), Status.Booked)
                .orElseThrow(() -> new RuntimeException("Активная бронь на место "
                        + request.getWorkplaceId() + " не найдена"));


        // продливаем бронь
        booking.setEndBooking(request.getEndBooking()); // новое время окончания

        return bookingRepository.save(booking);
    }
}
