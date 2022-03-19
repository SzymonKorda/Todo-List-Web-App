package com.szymon.todolist.service;

import com.szymon.todolist.payload.response.SimpleTaskResponse;
import com.szymon.todolist.payload.response.TaskCountResponse;
import com.szymon.todolist.payload.request.TaskRequest;
import com.szymon.todolist.payload.response.FullTaskResponse;

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
