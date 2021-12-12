package com.szymon.todolist.mapper;

import com.szymon.todolist.model.Task;
import com.szymon.todolist.payload.FullTaskResponse;
import com.szymon.todolist.payload.SimpleTaskResponse;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public FullTaskResponse mapTaskToFullTaskResponse(Task task) {
        return new FullTaskResponse.Builder(task.getId())
                .withTitle(task.getTitle())
                .withDescription(task.getDescription())
                .withCreatedOn(task.getCreatedOn())
                .withFinishedOn(task.getFinishedOn())
                .withIsActive(task.isActive())
                .build();
    }

    public SimpleTaskResponse mapTaskToSimpleTaskResponse(Task task) {
        return new SimpleTaskResponse.Builder(task.getId())
                .withTitle(task.getTitle())
                .withDescription(task.getDescription())
                .build();
    }
}
