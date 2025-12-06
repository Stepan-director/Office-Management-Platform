package com.example.Workplace_service;

import com.example.Workplace_service.dto.CreateWorkplaceRequest;
import com.example.Workplace_service.model.Workspace;
import com.example.Workplace_service.repository.WorkspaceRepository;
import com.example.Workplace_service.service.KafkaProducerService;
import com.example.Workplace_service.service.WorkspaceService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@SpringBootTest
class WorkplaceServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Mock
	private WorkspaceRepository workspaceRepository;

	@Mock
	private KafkaProducerService producerService;

	@InjectMocks
	private WorkspaceService workspaceService;

	// создать место
	@Test
	public void createWorkspaceTest(){

		String testWorkplaceId = "A-100";
		CreateWorkplaceRequest request = new CreateWorkplaceRequest(testWorkplaceId, 2);

		// проверка на сущестовование
		when(workspaceRepository.existsByWorkplaceId(testWorkplaceId)).thenReturn(false);

		// сохраняем
		Workspace savedWorkspace = new Workspace(1L, testWorkplaceId,2);
		when(workspaceRepository.save(any(Workspace.class))).thenReturn(savedWorkspace);

		// kafka
		doNothing().when(producerService).sendMessage(request);

		Workspace result = workspaceService.createWorkspace(request);

		assertNotNull(result);
		assertEquals(testWorkplaceId,result.getWorkplaceId());

		verify(workspaceRepository,times(1)).existsByWorkplaceId(testWorkplaceId);
		verify(workspaceRepository,times(1)).save(any(Workspace.class));
		verify(producerService,times(1)).sendMessage(request);

	}

	@Test
	public void createWorkspaceTest_ID_EXISTS(){

		String testWorkplaceId = "A-100";
		CreateWorkplaceRequest request = new CreateWorkplaceRequest(testWorkplaceId, 2);

		when(workspaceRepository.existsByWorkplaceId(testWorkplaceId)).thenReturn(true);

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			workspaceService.createWorkspace(request);
		});
		assertTrue(exception.getMessage().contains("Место с ID " + request.getWorkplaceId() + " уже существует"));

		verify(workspaceRepository, never()).save(any(Workspace.class));
		verify(producerService, never()).sendMessage(any());

	}

	// создать место и отправить через кафка
	@Test
	public void createWorkspaceTest_KAFKA_MESSAGE(){

		String testWorkplaceId = "A-100";
		CreateWorkplaceRequest request = new CreateWorkplaceRequest(testWorkplaceId, 2);

		when(workspaceRepository.existsByWorkplaceId(testWorkplaceId)).thenReturn(false);
		Workspace savedWorkspace = new Workspace(1L, testWorkplaceId, 2);

		when(workspaceRepository.save(any(Workspace.class))).thenReturn(savedWorkspace);

		doThrow(new RuntimeException("Кафка не работает")).when(producerService).sendMessage(request);

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			workspaceService.createWorkspace(request);
		});

		assertTrue(exception.getMessage().contains("Не удалось отправить сообщение в Kafka"));

		verify(workspaceRepository, times(1)).save(any(Workspace.class));
		verify(producerService, times(1)).sendMessage(request);

	}

	// посмотреть все места
	@Test
	public void getAllWorkspaceTest(){

		Workspace workspace1 = new Workspace(1L, "A-100", 2);
		Workspace workspace2 = new Workspace(2L, "Б-100", 3);

		List<Workspace> workspaceList = Arrays.asList(workspace1,workspace2);

		when(workspaceRepository.findAll()).thenReturn(workspaceList);

		List<Workspace> workspaces = workspaceService.getAllWorkspace();

		verify(workspaceRepository, times(1)).findAll();

		assertNotNull(workspaces);

		assertEquals(2, workspaces.size());

		assertEquals(workspaceList, workspaces);

	}

	// если список пустой
	@Test
	public void getAllWorkspaceTest_EMPTY_LIST(){

		when(workspaceRepository.findAll()).thenReturn(Arrays.asList());

		List<Workspace> workspaces = workspaceService.getAllWorkspace();

		verify(workspaceRepository, times(1)).findAll();

		assertNotNull(workspaces);
		assertTrue(workspaces.isEmpty());

	}

	// удаление места
	@Test
	public void deleteWorkspaceTest(){

		String testWorkplaceId = "A-100";
		Workspace workspace = new Workspace(1L, testWorkplaceId, 2);

		when(workspaceRepository.findByWorkplaceId(testWorkplaceId)).thenReturn(Optional.of(workspace));

		doNothing().when(workspaceRepository).deleteWorkspace(testWorkplaceId);

		workspaceService.deleteWorkspace(testWorkplaceId);

		verify(workspaceRepository, times(1)).findByWorkplaceId(testWorkplaceId);
		verify(workspaceRepository, times(1)).deleteWorkspace(testWorkplaceId);

	}

	// удаление если место не нвйдено
	@Test
	public void deleteWorkspaceTest_EMPTY(){

		String testWorkplaceId = "A-100";
		Workspace workspace = new Workspace();

		when(workspaceRepository.findByWorkplaceId(testWorkplaceId)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> workspaceService.deleteWorkspace(testWorkplaceId));

		assertTrue(exception.getMessage().contains("Место с номером: " + testWorkplaceId + " не найдено"));

		verify(workspaceRepository, times(1)).findByWorkplaceId(testWorkplaceId);
		verify(workspaceRepository, never()).deleteWorkspace(testWorkplaceId);
	}


}
