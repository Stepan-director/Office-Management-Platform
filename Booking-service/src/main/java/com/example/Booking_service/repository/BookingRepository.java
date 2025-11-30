package com.example.Booking_service.repository;

import com.example.Booking_service.model.Booking;
import com.example.Booking_service.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository  extends JpaRepository<Booking, Long> {

    boolean existsByWorkplaceId(String workplaceId);

    Optional<Booking> findByWorkplaceIdAndEmployeeIdAndStatus(String workplaceId, String employeeId, Status status);
}
