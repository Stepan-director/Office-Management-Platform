package com.example.Authentication_service.service;


import com.example.Authentication_service.client.UserServiceClient;
import com.example.Authentication_service.dto.CreateProfileRequest;
import com.example.Authentication_service.dto.LoginRequest;
import com.example.Authentication_service.dto.LoginResponse;
import com.example.Authentication_service.dto.RegisterRequest;
import com.example.Authentication_service.model.AuthUser;
import com.example.Authentication_service.model.Role;
import com.example.Authentication_service.repository.AuthRepository;
import com.example.Authentication_service.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserServiceClient userServiceClient;

    private Random random = new Random();


    // это тоже делает только админ
    // также preauthorize, настраиваем security, но у нас уже не надо здесь токены использовать, наверно
    @Transactional
    public AuthUser registerUser(RegisterRequest request) {
        String employeeId;
        do {
            employeeId = generateEmployeeId(request.getRole());
        } while (authRepository.existsByEmployeeId(employeeId));

        AuthUser authUser = new AuthUser();
        authUser.setEmployeeId(employeeId);
        authUser.setFullName(request.getFullName());
        authUser.setRole(request.getRole());
        authUser.setActive(true);
        authUser.setCreatedAt(LocalDateTime.now());
        authUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        AuthUser savedUser = authRepository.save(authUser);
        if (userServiceClient != null){
            try {
                userServiceClient.createUserProfile(
                        new CreateProfileRequest(
                                savedUser.getId(),
                                savedUser.getEmployeeId(),
                                savedUser.getFullName(),
                                savedUser.getRole(),
                                savedUser.isActive(),
                                savedUser.getCreatedAt()
                        )
                );
            } catch (Exception e) {
                authRepository.delete(savedUser);
                throw new RuntimeException("Ошибка создания профиля: " + e.getMessage());
            }
        }


        return savedUser;
    }

    private String generateEmployeeId(Role role) {
        String prefix = "EMP";
        return prefix + "-" + (1000 + random.nextInt(9000));
    }

    public LoginResponse loginUser(LoginRequest request){

        AuthUser authUser = authRepository.findByEmployeeId(request.getEmployeeId()).orElseThrow(()
                -> new RuntimeException("Пользователь с табельным номером: " + request.getEmployeeId() + " не найден"));

        if(!passwordEncoder.matches(request.getPassword(), authUser.getPasswordHash())){
            throw new RuntimeException("Неверный пароль! Попробуйте ввести снова");
        }

        if(!authUser.isActive()){
            throw new RuntimeException("Пользователь не имеет доступ к профилю");
        }

        String token = jwtUtil.generateToken(authUser);

        // возвращаем ответ с данными для входа
        return new LoginResponse(
                token,
                authUser.getEmployeeId()
        );
    }


}
