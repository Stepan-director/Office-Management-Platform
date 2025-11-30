package com.example.User_service.service;

import com.example.User_service.dto.BookingCreatedEvent;
import com.example.User_service.dto.UpdateProfileRequest;
import com.example.User_service.model.User;
import com.example.User_service.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final String USER_BOOKING_TOPIC = "user-booking-topic";

    @Autowired
    UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    // может сделать транзакцию  в сервисе
    @KafkaListener(topics = "user-booking-topic")
    public void userBookingWorkplace(String message) {

        try{
            BookingCreatedEvent event = objectMapper.readValue(message, BookingCreatedEvent.class);
            System.out.println("Забронирвоано новое место:" + event.getWorkplaceId() + "\nПользователь: " + event.getEmployeeId());

            User user = userRepository.findByEmployeeId(event.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException(
                            "Пользователь с табельным номером " + event.getEmployeeId() + " не найден"));

            user.setWorkplaceId(event.getWorkplaceId());
            userRepository.save(user);
            System.out.println("Рабочее место забронировано для пользователя: " + event.getEmployeeId());

        } catch (Exception e){
            System.err.println("Ошибка обработки сообщения: " + e.getMessage());
        }
    }
}
