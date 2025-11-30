package com.example.Booking_service.dto;

public class BookingCreatedEvent {
    private String employeeId; // личный табельный номер из auth service
    private String workplaceId;

    public BookingCreatedEvent(String employeeId, String workplaceId) {
        this.employeeId = employeeId;
        this.workplaceId = workplaceId;
    }

    public BookingCreatedEvent() {
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
}
