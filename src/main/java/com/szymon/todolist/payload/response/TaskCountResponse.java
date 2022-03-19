package com.szymon.todolist.payload.response;

import lombok.*;

@Data
@Builder
public class TaskCountResponse {
    private final long activeCount;
    private final long finishedCount;
}
