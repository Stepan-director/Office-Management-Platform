package com.example.Workplace_service.dto;

public class WorkspaceCreatedEvent {
    private String workplaceId;
    private int floor;

    public WorkspaceCreatedEvent(String workplaceId, int floor) {
        this.workplaceId = workplaceId;
        this.floor = floor;
    }

    public WorkspaceCreatedEvent() {
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
}