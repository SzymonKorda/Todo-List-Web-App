package com.szymon.todolist.service;

import com.szymon.todolist.payload.SimpleTaskResponse;
import com.szymon.todolist.payload.TaskCountResponse;
import com.szymon.todolist.payload.TaskRequest;
import com.szymon.todolist.payload.FullTaskResponse;

import java.util.List;

public interface TaskService {
    void newTask(TaskRequest taskRequest);
    FullTaskResponse getTask(Integer id);
    void updateTask(Integer id, TaskRequest taskRequest);
    void deleteTask(Integer id);
    void finishTask(Integer id);
    List<FullTaskResponse> getUserTasks();
    List<SimpleTaskResponse> getUserActiveTasks();
    List<FullTaskResponse> getUserFinishedTasks();
    TaskCountResponse getUserTaskCount();
}
