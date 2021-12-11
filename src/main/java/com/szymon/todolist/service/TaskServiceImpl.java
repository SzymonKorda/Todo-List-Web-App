package com.szymon.todolist.service;

import com.szymon.todolist.exception.NotFoundException;
import com.szymon.todolist.model.Task;
import com.szymon.todolist.payload.TaskRequest;
import com.szymon.todolist.payload.TaskResponse;
import com.szymon.todolist.reposotiry.TaskRepository;
import com.szymon.todolist.security.User;
import com.szymon.todolist.security.UserDetailsImpl;
import com.szymon.todolist.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService{

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void newTask(TaskRequest taskRequest) {
        User user = getUser();
        Task task = buildTask(taskRequest, user);
        user.getTasks().add(task);
        userRepository.save(user);
    }

    @Override
    public TaskResponse getTask(Integer id) {
        return mapTaskToTaskResponse(getTaskByUser(id, getUser()));
    }

    @Override
    public Task updateTask(Integer id, TaskRequest taskRequest) {
        User user = getUser();
        Task task = getTaskByUser(id, user);
        updateDescriptionAndTitle(taskRequest, task);
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Integer id) {
        User user = getUser();
        Task task = getTaskByUser(id, user);
        taskRepository.delete(task);
    }

    @Override
    public List<Task> getUserTasks() {
        User user = getUser();
        return user.getTasks();
    }

    @Override
    public void finishTask(Integer id) {
        User user = getUser();
        Task task = getTaskByUser(id, user);
        task.setActive(false);
        task.setFinishedOn(new Date());
        taskRepository.save(task);
    }

    private User getUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));
    }

    private Task getTaskByUser(Integer id, User user) {
        return taskRepository
                .findTaskByUserAndId(user, id)
                .orElseThrow(() -> new NotFoundException(String.format("Task with id %d not found", id)));
    }

    private Task buildTask(TaskRequest taskRequest, User user) {
        return new Task
                .Builder()
                .withTitle(taskRequest.getTitle())
                .withDescription(taskRequest.getDescription())
                .withUser(user)
                .build();
    }

    private void updateDescriptionAndTitle(TaskRequest taskRequest, Task task) {
        if (taskRequest.getTitle() != null) {
            task.setTitle(taskRequest.getTitle());
        }
        if (taskRequest.getDescription() != null) {
            task.setDescription(taskRequest.getDescription());
        }
    }

    private TaskResponse mapTaskToTaskResponse(Task task) {
        return new TaskResponse.Builder(task.getId())
                .withTitle(task.getTitle())
                .withDescription(task.getDescription())
                .withCreatedOn(task.getCreatedOn())
                .withFinishedOn(task.getFinishedOn())
                .withIsActive(task.isActive())
                .build();
    }
}