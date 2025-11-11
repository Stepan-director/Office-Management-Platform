package com.example.Workplace_service.controller;


import com.example.Workplace_service.dto.CreateWorkplaceRequest;
import com.example.Workplace_service.dto.WorkspaceResponse;
import com.example.Workplace_service.model.Workspace;
import com.example.Workplace_service.service.WorkspaceService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @PostMapping()
    // проверяет, что пользователь аутентифицирован
    // проверяет, что роль в токене - 'ADMIN'
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> createWorkspace(
            @RequestBody CreateWorkplaceRequest createWorkplaceRequest) {

        try {
            Workspace workspace = workspaceService.createWorkspace(createWorkplaceRequest);

            return ResponseEntity.ok(new WorkspaceResponse(workspace));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/delete/workspace")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional
    public ResponseEntity<?> deleteWorkspace(String workplaceId){
        try{
            workspaceService.deleteWorkspace(workplaceId);
            return ResponseEntity.ok("Место с номером:" + workplaceId + " удалён");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
