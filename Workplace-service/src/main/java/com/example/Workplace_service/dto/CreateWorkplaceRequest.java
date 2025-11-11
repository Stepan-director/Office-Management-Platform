package com.example.Workplace_service.dto;

// это будет на фронтенде
public class CreateWorkplaceRequest {
    private String workplaceId;
    private int floor;

    public CreateWorkplaceRequest(String workplaceId, int floor) {
        this.workplaceId = workplaceId;
        this.floor = floor;
    }

    public CreateWorkplaceRequest() {
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
