package com.example.Workplace_service.repository;

import com.example.Workplace_service.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    boolean existsByWorkplaceId(String workplaceId);

    @Modifying
    @Query(value = "Delete from workspace where workplace_id = :workplaceId", nativeQuery = true)
    void deleteWorkspace(@Param("workplaceId") String workplaceId);

    Optional<Workspace> findByWorkplaceId(String workplaceId);
}
