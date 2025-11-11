package com.example.Authentication_service.client;

import com.example.Authentication_service.dto.CreateProfileRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service", url = "${user.service.url:http://localhost:8081}")
public interface UserServiceClient {

    @PostMapping("/api/users/profiles")
    void createUserProfile(CreateProfileRequest request);

}