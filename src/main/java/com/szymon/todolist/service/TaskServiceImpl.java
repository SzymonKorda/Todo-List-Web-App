package com.szymon.todolist.service;

import com.szymon.todolist.exception.NotFoundException;
import com.szymon.todolist.mapper.TaskMapper;
import com.szymon.todolist.model.Task;
import com.szymon.todolist.payload.SimpleTaskResponse;
import com.szymon.todolist.payload.TaskCountResponse;
import com.szymon.todolist.payload.TaskRequest;
import com.szymon.todolist.payload.FullTaskResponse;
import com.szymon.todolist.reposotiry.TaskRepository;
import com.szymon.todolist.security.User;
import com.szymon.todolist.security.UserDetailsImpl;
import com.szymon.todolist.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.function.Predicate.*;


@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void newTask(TaskRequest taskRequest) {
        User user = getCurrentUser();
        user.getTasks().add(buildTask(taskRequest, user));
        userRepository.save(user);
    }

    @Override
    public FullTaskResponse getTask(Integer id) {
        return TaskMapper.mapTaskToFullTaskResponse(getTaskByUser(id, getCurrentUser()));
    }

    @Override
    public void updateTask(Integer id, TaskRequest taskRequest) {
        Task updatedTask = getTaskByUser(id, getCurrentUser());
        updatedTask.setTitle(taskRequest.getTitle());
        updatedTask.setDescription(taskRequest.getDescription());
        taskRepository.save(updatedTask);
    }

    @Override
    public void deleteTask(Integer id) {
        taskRepository.delete(getTaskByUser(id, getCurrentUser()));
    }

    @Override
    public List<FullTaskResponse> getUserTasks() {
        return getCurrentUser().getTasks().stream()
                .map(TaskMapper::mapTaskToFullTaskResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SimpleTaskResponse> getUserActiveTasks() {
        return getCurrentUser().getTasks().stream()
                .filter(Task::isActive)
                .map(TaskMapper::mapTaskToSimpleTaskResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FullTaskResponse> getUserFinishedTasks() {
        return getCurrentUser().getTasks().stream()
                .filter(not(Task::isActive))
                .map(TaskMapper::mapTaskToFullTaskResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void finishTask(Integer id) {
        Task task = getTaskByUser(id, getCurrentUser());
        task.setActive(false);
        task.setFinishedOn(new Date());
        taskRepository.save(task);
    }

    @Override
    public TaskCountResponse getUserTaskCount() {
        User user = getCurrentUser();
        return prepareTaskCountResponse(user);
    }

    private User getCurrentUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));
    }

    private TaskCountResponse prepareTaskCountResponse(User user) {
        TaskCountResponse taskCount = new TaskCountResponse();
        taskCount.setActiveCount(user.getTasks().stream().filter(Task::isActive).count());
        taskCount.setFinishedCount(user.getTasks().stream().filter(not(Task::isActive)).count());
        return taskCount;
    }

    private Task getTaskByUser(Integer id, User user) {
        return taskRepository
                .findTaskByUserAndId(user, id)
                .orElseThrow(() -> new NotFoundException(String.format("Task with id %d not found", id)));
    }

    private Task buildTask(TaskRequest taskRequest, User user) {
        return new Task.Builder()
                .withTitle(taskRequest.getTitle())
                .withDescription(taskRequest.getDescription())
                .withUser(user)
                .build();
    }
}