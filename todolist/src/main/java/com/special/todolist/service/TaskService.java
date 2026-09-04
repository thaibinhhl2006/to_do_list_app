package com.special.todolist.service;

import com.special.todolist.dto.request.CreateTaskRequest;
import com.special.todolist.dto.request.UpdateTaskRequest;
import com.special.todolist.dto.response.TaskResponse;
import com.special.todolist.entity.Task;
import com.special.todolist.exception.ResourceNotFoundException;
import com.special.todolist.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<TaskResponse> findAll() {
        return repository.findAll().stream()
                .map(TaskResponse::from)
                .collect(Collectors.toList());
    }

    public TaskResponse findById(UUID id) {
        return TaskResponse.from(getEntity(id));
    }

    public TaskResponse create(CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDueDate(request.dueDate());
        return TaskResponse.from(repository.save(task));
    }

    public TaskResponse update(UUID id, UpdateTaskRequest request) {
        Task task = getEntity(id);
        if (request.title() != null) task.setTitle(request.title());
        if (request.description() != null) task.setDescription(request.description());
        if (request.status() != null) task.setStatus(request.status());
        if (request.dueDate() != null) task.setDueDate(request.dueDate());
        return TaskResponse.from(repository.save(task));
    }

    public void delete(UUID id) {
        repository.delete(getEntity(id));
    }

    private Task getEntity(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
    }
}