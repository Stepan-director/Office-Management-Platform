package com.example.Authentication_service.repository;

import com.example.Authentication_service.model.AuthUser;
import com.example.Authentication_service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthRepository extends JpaRepository<AuthUser, Long> {

    @Query("SELECT u FROM AuthUser u WHERE u.employeeId = :employeeId")
    Optional<AuthUser> findByEmployeeId(String employeeId);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM AuthUser u WHERE u.employeeId = :employeeId")
    boolean existsByEmployeeId(String employeeId);

    List<AuthUser> findByRole(Role role);

    List<AuthUser> findByActiveTrue();

    List<AuthUser> findByFullNameContainingIgnoreCase(String fullName);


}
