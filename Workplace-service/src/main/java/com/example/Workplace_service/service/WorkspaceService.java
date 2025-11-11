package com.example.Workplace_service.service;

import com.example.Workplace_service.dto.CreateWorkplaceRequest;
import com.example.Workplace_service.dto.WorkspaceCreatedEvent;
import com.example.Workplace_service.model.Workspace;
import com.example.Workplace_service.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkspaceService {

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private KafkaProducerService producerService;

    private static final String WORKSPACE_TOPIC = "workspace-events";

    // создание рабочего места
    @Transactional
    public Workspace createWorkspace(CreateWorkplaceRequest request){

        if(workspaceRepository.existsByWorkplaceId(request.getWorkplaceId())){
            throw new RuntimeException("Место с ID " + request.getWorkplaceId() + " уже сущетсвует");
        }
        Workspace workspace = new Workspace();
        workspace.setWorkplaceId(request.getWorkplaceId());
        workspace.setFloor(request.getFloor());

        Workspace savedWorkspace =  workspaceRepository.save(workspace);

        try{
            producerService.sendMessage(request);
            System.out.println("Создано новое место: " + request.getWorkplaceId() +
                    "\n Событие отправлено в Kafka.");
        } catch (Exception e){
            System.err.println("Ошибка, сообшение не отправлено в Kafka, откат транзакции " + e.getMessage());
            throw new RuntimeException("Не удалось отправить сообщение в Kafka " + e.getMessage());
        }

        return savedWorkspace;


    }

    // посмотреть все места
    public List<Workspace> getAllWorkspace(){
        return workspaceRepository.findAll();
    }

    public void deleteWorkspace(String workplaceId){

        Workspace workspace = workspaceRepository.findByWorkplaceId(workplaceId).orElseThrow(()
        -> new RuntimeException("Место с номером: " + workplaceId + " не найдено"));

        workspaceRepository.deleteWorkspace(workplaceId);
    }




}

