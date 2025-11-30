package com.example.Booking_service.controller;

import com.example.Booking_service.dto.CreateBookingRequest;
import com.example.Booking_service.model.Booking;
import com.example.Booking_service.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;


    // создаём бронь
    @PostMapping()
    public ResponseEntity<?> createBooking(@RequestBody CreateBookingRequest request){
        try {
             Booking booking = bookingService.createBooking(request);
            return ResponseEntity.ok(new CreateBookingRequest());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
