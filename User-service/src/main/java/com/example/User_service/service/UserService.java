package com.example.User_service.service;

import com.example.User_service.dto.CreateProfileRequest;
import com.example.User_service.dto.UserProfileResponse;
import com.example.User_service.model.User;
import com.example.User_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User createUserProfile(CreateProfileRequest request){

        // сделать проверку, нет ли профиля с таким userId,
        // чтобы не было одинаковых

        User user = new User();
        user.setId(request.getId());
        user.setEmployeeId(request.getEmployeeId());
        user.setFullName(request.getFullName());
        user.setRole(request.getRole());
        user.setActive(request.isActive());
        user.setCreatedAt(LocalDateTime.now());
        // email и phone заполнятся специалистом
        return userRepository.save(user);
    }

    public UserProfileResponse getUserProfileByEmployeeId(String employeeId) {
        User user = userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Профиль с табельным номером " + employeeId + " не найден"));

        return new UserProfileResponse(user);
    }

}
