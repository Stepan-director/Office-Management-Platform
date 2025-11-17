package com.example.Booking_service.service;


import com.example.Booking_service.dto.CreateBookingRequest;
import com.example.Booking_service.model.Booking;
import com.example.Booking_service.model.Status;
import com.example.Booking_service.repository.BookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    // бронируем место
    @Transactional
    public Booking createBooking(CreateBookingRequest request){
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
        booking.setWorkplaceId(request.getWorkplaceId());
        booking.setFloor(request.getFloor());
        booking.setStatus(Status.Booked);
        booking.setDate(LocalDate.now());
        booking.setStartBooking(LocalTime.now());
        booking.setEndBooking(request.getEndBooking());

        return bookingRepository.save(booking);

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

        // отмена брони
        Booking booking = new Booking();
        booking.setWorkplaceId(request.getWorkplaceId());
        booking.setFloor(request.getFloor());
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

        // продливаем бронь
        Booking booking = new Booking();
        booking.setWorkplaceId(request.getWorkplaceId());
        booking.setFloor(request.getFloor());
        booking.setStatus(Status.Booked);
        booking.setDate(LocalDate.now());
        booking.setEndBooking(LocalTime.now());

        return bookingRepository.save(booking);
    }
}
