package com.special.todolist.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;

public record CreateTaskRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255)
        String title,

        @NotBlank(message = "Description is required")
        @Size(max = 2255)
        String description,

        Instant dueDate
) {
}
