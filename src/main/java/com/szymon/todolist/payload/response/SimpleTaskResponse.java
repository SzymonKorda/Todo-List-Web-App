package com.szymon.todolist.payload.response;

import lombok.*;

@Data
@Builder
public class SimpleTaskResponse {
    private final Integer id;
    private final String title;
    private final String description;
}
