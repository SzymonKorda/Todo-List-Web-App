package com.szymon.todolist.payload.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class TaskRequest {
    @NotBlank(message = "Title can't be empty!")
    @Size(max = 50)
    private final String title;

    @NotBlank(message = "Description can't be empty!")
    @Size(max = 200)
    private final String description;

}
