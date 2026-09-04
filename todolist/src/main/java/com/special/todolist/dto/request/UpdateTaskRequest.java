package com.special.todolist.dto.request;

import com.special.todolist.domain.TaskStatus;
import jakarta.validation.constraints.Size;
import java.time.Instant;

public record UpdateTaskRequest(
        @Size(max = 255)
        String title,

        @Size(max = 2255)
        String description,

        TaskStatus status,

        Instant dueDate
) {
}
