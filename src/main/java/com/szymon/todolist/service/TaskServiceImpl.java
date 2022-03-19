package com.szymon.todolist.service;

import com.szymon.todolist.exception.NotFoundException;
import com.szymon.todolist.utils.mapper.TaskMapper;
import com.szymon.todolist.model.Task;
import com.szymon.todolist.payload.response.FullTaskResponse;
import com.szymon.todolist.payload.response.SimpleTaskResponse;
import com.szymon.todolist.payload.response.TaskCountResponse;
import com.szymon.todolist.payload.request.TaskRequest;
import com.szymon.todolist.reposotiry.TaskRepository;
import com.szymon.todolist.model.User;
import com.szymon.todolist.security.user.UserDetailsImpl;
import com.szymon.todolist.reposotiry.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

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
        return TaskCountResponse.builder()
                .activeCount(user.getTasks().stream().filter(Task::isActive).count())
                .finishedCount((user.getTasks().stream().filter(not(Task::isActive)).count()))
                .build();
    }

    private Task getTaskByUser(Integer id, User user) {
        return taskRepository
                .findTaskByUserAndId(user, id)
                .orElseThrow(() -> new NotFoundException(String.format("Task with id %d not found", id)));
    }

    private Task buildTask(TaskRequest taskRequest, User user) {
        return Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .user(user)
                .build();
    }
}