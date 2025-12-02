package com.example.Booking_service.service;

import com.example.Booking_service.dto.WorkspaceCreatedEvent;
import com.example.Booking_service.model.Booking;
import com.example.Booking_service.model.Status;
import com.example.Booking_service.model.WorkplaceInventory;
import com.example.Booking_service.repository.BookingRepository;
import com.example.Booking_service.repository.WorkplaceInventoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final String TOPIC = "workplace_add";

    @Autowired
    private WorkplaceInventoryRepository workplaceInventoryRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    @KafkaListener(topics = "workplace_add")
    public void receiveWorkspace(String message) {
        try {
            // Десериализуем JSON строку в объект
            WorkspaceCreatedEvent event = objectMapper.readValue(message, WorkspaceCreatedEvent.class);

            if (workplaceInventoryRepository.existsByWorkplaceId(event.getWorkplaceId())) {
                System.out.println("Место " + event.getWorkplaceId() + " уже синхронизировано. Игнорируем.");
                return;
            }
            else{
                System.out.println("Получено событие о создании рабочего места: " + event.getWorkplaceId());
            }


            WorkplaceInventory inventory = new WorkplaceInventory();
            inventory.setWorkplaceId(event.getWorkplaceId());
            inventory.setFloor(event.getFloor());
            inventory.setStatus(Status.Free);

            workplaceInventoryRepository.save(inventory);


            System.out.println("Локальная копия места " + event.getWorkplaceId() + " сохранена в базу данных WorkplaceInventory.");

        } catch (Exception e) {
            System.err.println("Ошибка обработки сообщения: " + e.getMessage());
        }
    }
}