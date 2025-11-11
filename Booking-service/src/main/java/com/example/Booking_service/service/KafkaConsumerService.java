package com.example.Booking_service.service;

import com.example.Booking_service.dto.WorkspaceCreatedEvent;
import com.example.Booking_service.model.Booking;
import com.example.Booking_service.repository.BookingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final String TOPIC = "workplace_add";

    @Autowired
    private BookingRepository bookingRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "workplace_add")
    public void receiveWorkspace(String message) {
        try {
            // Десериализуем JSON строку в объект
            WorkspaceCreatedEvent event = objectMapper.readValue(message, WorkspaceCreatedEvent.class);

            System.out.println("Получено событие о создании рабочего места: " + event.getWorkplaceId());

            Booking booking = new Booking();
            booking.setWorkplaceId(event.getWorkplaceId());
            booking.setFloor(event.getFloor());

            bookingRepository.save(booking);

            System.out.println("Локальная копия места " + event.getWorkplaceId() + " сохранена в базу данных Booking.");

        } catch (Exception e) {
            System.err.println("Ошибка обработки сообщения: " + e.getMessage());
        }
    }
}