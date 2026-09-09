package com.special.todolist.dto.response;

import java.time.Instant;
import java.util.UUID;
import com.special.todolist.domain.TaskStatus;
import com.special.todolist.entity.Task;

public record TaskResponse(
        UUID id,
        String title,
        String description,
        TaskStatus status,
        Instant dueDate,
        Instant createdAt,
        Instant updatedAt
) {
    public static TaskResponse from(Task t) {
        return new TaskResponse(
                t.getId(), t.getTitle(), t.getDescription(),
                t.getStatus(), t.getDueDate(),
                t.getCreatedAt(), t.getUpdatedAt()
        );
    }
}
