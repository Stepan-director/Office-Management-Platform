package com.example.Booking_service.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = true)
    private String employeeId; // личный табельный номер из auth service

    @Column(name = "workplace_id", nullable = false)
    private String workplaceId; // номер места из workspace service

    private int floor; // этаж из workspace service

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDate date; // дата брони места

    @Column(name = "start_booking")
    private LocalTime startBooking; // начало брони

    @Column(name = "end_booking")
    private LocalTime endBooking; // конец брони

    public Booking(Long id, String employeeId, String workplaceId, int floor, Status status, LocalDate date, LocalTime startBooking, LocalTime endBooking) {
        this.id = id;
        this.employeeId = employeeId;
        this.workplaceId = workplaceId;
        this.floor = floor;
        this.status = status;
        this.date = date;
        this.startBooking = startBooking;
        this.endBooking = endBooking;
    }

    public Booking() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getWorkplaceId() {
        return workplaceId;
    }

    public void setWorkplaceId(String workplaceId) {
        this.workplaceId = workplaceId;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartBooking() {
        return startBooking;
    }

    public void setStartBooking(LocalTime startBooking) {
        this.startBooking = startBooking;
    }

    public LocalTime getEndBooking() {
        return endBooking;
    }

    public void setEndBooking(LocalTime endBooking) {
        this.endBooking = endBooking;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
