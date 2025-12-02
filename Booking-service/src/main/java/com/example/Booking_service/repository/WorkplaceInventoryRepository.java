package com.example.Booking_service.repository;

import com.example.Booking_service.model.Booking;
import com.example.Booking_service.model.Status;
import com.example.Booking_service.model.WorkplaceInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface WorkplaceInventoryRepository extends JpaRepository<WorkplaceInventory, Long> {

    boolean existsByWorkplaceId(String workplaceId);

    Optional<WorkplaceInventory> findByWorkplaceId(String workplaceId);


}
