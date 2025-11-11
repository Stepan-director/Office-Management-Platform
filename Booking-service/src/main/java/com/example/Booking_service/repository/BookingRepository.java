package com.example.Booking_service.repository;

import com.example.Booking_service.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository  extends JpaRepository<Booking, Long> {

    boolean existsByWorkplaceId(String workplaceId);

}
