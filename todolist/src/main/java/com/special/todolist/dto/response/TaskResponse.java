package com.special.todolist.dto.response;

import java.time.Instant;
import java.util.UUID;
import com.special.todolist.domain.TaskStatus;

public record TaskResponse(
        UUID id,
        String title,
        String description,
        TaskStatus status,
        Instant dueDate,
        Instant createdAt,
        Instant updatedAt
) {
}
