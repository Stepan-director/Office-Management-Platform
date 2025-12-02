package com.example.Booking_service.repository;

import com.example.Booking_service.model.Booking;
import com.example.Booking_service.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository  extends JpaRepository<Booking, Long> {

    boolean existsByWorkplaceId(String workplaceId);

    Optional<Booking> findByWorkplaceIdAndEmployeeIdAndStatus(String workplaceId, String employeeId, Status status);

    Optional<Booking> findByWorkplaceId(String workplaceId);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END FROM Booking b " +
            "WHERE b.workplaceId = :workplaceId AND b.date = :date " +
            "AND b.status = 'Booked' " + "AND (" + "(:startBooking < b.endBooking AND :endBooking > b.startBooking)" + ")")
    boolean isTimeSlotBooked(@Param("workplaceId") String workplaceId,
                             @Param("date") LocalDate date,
                             @Param("startBooking") LocalTime startBooking,
                             @Param("endBooking") LocalTime endBooking);
}
