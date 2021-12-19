package com.szymon.todolist.mapper;

import com.szymon.todolist.model.Task;
import com.szymon.todolist.payload.FullTaskResponse;
import com.szymon.todolist.payload.SimpleTaskResponse;

public class TaskMapper {
    public static FullTaskResponse mapTaskToFullTaskResponse(Task task) {
        return new FullTaskResponse.Builder(task.getId())
                .withTitle(task.getTitle())
                .withDescription(task.getDescription())
                .withCreatedOn(task.getCreatedOn())
                .withFinishedOn(task.getFinishedOn())
                .withIsActive(task.isActive())
                .build();
    }

    public static SimpleTaskResponse mapTaskToSimpleTaskResponse(Task task) {
        return new SimpleTaskResponse.Builder(task.getId())
                .withTitle(task.getTitle())
                .withDescription(task.getDescription())
                .build();
    }
}
