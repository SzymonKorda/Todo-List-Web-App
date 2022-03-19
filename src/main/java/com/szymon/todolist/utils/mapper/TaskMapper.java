package com.szymon.todolist.utils.mapper;

import com.szymon.todolist.model.Task;
import com.szymon.todolist.payload.response.FullTaskResponse;
import com.szymon.todolist.payload.response.SimpleTaskResponse;

public class TaskMapper {
    public static FullTaskResponse mapTaskToFullTaskResponse(Task task) {
        return FullTaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .createdOn(task.getCreatedOn())
                .finishedOn(task.getFinishedOn())
                .isActive(task.isActive())
                .build();
    }

    public static SimpleTaskResponse mapTaskToSimpleTaskResponse(Task task) {
        return SimpleTaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .build();
    }
}
