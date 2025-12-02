package com.example.Booking_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "workplace_inventory")
public class WorkplaceInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workplace_id", nullable = false, unique = true)
    private String workplaceId;

    private int floor;

    @Enumerated(EnumType.STRING)
    private Status status;

    public WorkplaceInventory(Long id, String workplaceId, int floor, Status status) {
        this.id = id;
        this.workplaceId = workplaceId;
        this.floor = floor;
        this.status = status;
    }

    public WorkplaceInventory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

