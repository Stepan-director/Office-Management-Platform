package com.example.Workplace_service.dto;

import com.example.Workplace_service.model.Workspace;


// то, что можно показывать
public class WorkspaceResponse {
    private Long id;
    private String workplaceId;
    private int floor;

    public WorkspaceResponse(Workspace workspace) {
        this.id = workspace.getId();
        this.workplaceId = workspace.getWorkplaceId();
        this.floor = workspace.getFloor();
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
}
