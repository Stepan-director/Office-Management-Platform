package com.example.Booking_service.dto;

import com.example.Booking_service.model.Status;

import java.time.LocalTime;

// это будет на фронтенде
public class CreateBookingRequest {
    private String employeeId;
    private String workplaceId;
    private int floor;
    private Status status;
    private LocalTime startBooking;
    private LocalTime endBooking;

    public CreateBookingRequest(String employeeId, String workplaceId, int floor, Status status, LocalTime startBooking, LocalTime endBooking) {
        this.employeeId = employeeId;
        this.workplaceId = workplaceId;
        this.floor = floor;
        this.status = status;
        this.startBooking = startBooking;
        this.endBooking = endBooking;
    }

    public CreateBookingRequest() {
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
