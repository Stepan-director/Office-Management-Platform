package com.example.Booking_service.controller;

import com.example.Booking_service.dto.CreateBookingRequest;
import com.example.Booking_service.model.Booking;
import com.example.Booking_service.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @DeleteMapping("/{workplaceId}")
    public ResponseEntity<?> cancellationBooking(@PathVariable String workplaceId) {
        try {
            bookingService.cancellationBooking(workplaceId);
            return ResponseEntity.ok().body(Map.of(
                    "message", "Бронирование успешно отменено",
                    "workplaceId", workplaceId
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
