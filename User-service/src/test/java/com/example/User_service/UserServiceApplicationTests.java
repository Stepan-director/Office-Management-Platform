package com.example.User_service;

import com.example.User_service.dto.CreateProfileRequest;
import com.example.User_service.dto.UserProfileResponse;
import com.example.User_service.model.User;
import com.example.User_service.repository.UserRepository;
import com.example.User_service.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.User_service.model.Role.ADMIN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@SpringBootTest
class UserServiceApplicationTests {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@Test
	void contextLoads() {
	}

	@Test
	public void createUserProfileTest() {

		CreateProfileRequest request = new CreateProfileRequest(
				1L,
				"EMP-600",
				"Григорий",
				ADMIN,
				true,
				LocalDateTime.now()
		);

		when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
			return invocation.getArgument(0);
		});

		User result = userService.createUserProfile(request);

		assertNotNull(result);
		assertEquals(request.getId(), result.getId());
		assertEquals(request.getEmployeeId(), result.getEmployeeId());
		assertEquals(request.getFullName(), result.getFullName());
		assertEquals(request.getRole(), result.getRole());
		assertEquals(request.isActive(), result.isActive());
		assertNotNull(result.getCreatedAt());

		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	public void getUserProfileByEmployeeIdTest() {

		String employeeId = "EMP-600";

		User testUser = new User();
		testUser.setId(1L);
		testUser.setEmployeeId(employeeId);
		testUser.setFullName("Григорий");
		testUser.setRole(ADMIN);
		testUser.setActive(true);
		testUser.setCreatedAt(LocalDateTime.now());

		when(userRepository.findByEmployeeId(employeeId)).thenReturn(Optional.of(testUser));

		UserProfileResponse response = userService.getUserProfileByEmployeeId(employeeId);

		assertNotNull(response);
		assertEquals(testUser.getId(), response.getId());
		assertEquals(testUser.getEmployeeId(), response.getEmployeeId());
		assertEquals(testUser.getFullName(), response.getFullName());
		assertEquals(testUser.getRole(), response.getRole());
		assertEquals(testUser.isActive(), response.isActive());

		verify(userRepository, times(1)).findByEmployeeId(employeeId);
	}
}
