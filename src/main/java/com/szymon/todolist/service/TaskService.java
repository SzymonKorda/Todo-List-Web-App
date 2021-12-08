package com.szymon.todolist.service;

import com.szymon.todolist.model.Task;
import com.szymon.todolist.payload.TaskRequest;

import java.util.List;

public interface TaskService {
    Task newTask(TaskRequest taskRequest);
    Task getTask(Integer id);
    Task updateTask(Integer id, TaskRequest taskRequest);
    void deleteTask(Integer id);
    void finishTask(Integer id);
    List<Task> getUserTasks();
}
