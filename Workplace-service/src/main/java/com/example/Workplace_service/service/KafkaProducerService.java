package com.example.Workplace_service.service;

import com.example.Workplace_service.dto.CreateWorkplaceRequest;
import com.example.Workplace_service.dto.WorkspaceCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final String TOPIC = "workplace_add";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(CreateWorkplaceRequest request){
        try {
            WorkspaceCreatedEvent event = new WorkspaceCreatedEvent();
            event.setWorkplaceId(request.getWorkplaceId());
            event.setFloor(request.getFloor());

            String eventJson = objectMapper.writeValueAsString(event);

            kafkaTemplate.send(TOPIC, event.getWorkplaceId(), eventJson);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка отправки сообщения в Kafka", e);
        }
    }
}