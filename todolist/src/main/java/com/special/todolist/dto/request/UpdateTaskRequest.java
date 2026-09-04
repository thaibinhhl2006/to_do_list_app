package com.special.todolist.dto.request;

import jakarta.validation.constraints.Size;
import java.time.Instant;

public record UpdateTaskRequest(
        @Size(max = 255)
        String title,

        @Size(max = 2255)
        String description,

        String status,

        Instant dueDate
) {
}
