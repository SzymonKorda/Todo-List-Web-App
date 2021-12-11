package com.szymon.todolist.service;

import com.szymon.todolist.model.Task;
import com.szymon.todolist.payload.TaskRequest;
import com.szymon.todolist.payload.TaskResponse;

import java.util.List;

public interface TaskService {
    void newTask(TaskRequest taskRequest);
    TaskResponse getTask(Integer id);
    Task updateTask(Integer id, TaskRequest taskRequest);
    void deleteTask(Integer id);
    void finishTask(Integer id);
    List<Task> getUserTasks();
}
