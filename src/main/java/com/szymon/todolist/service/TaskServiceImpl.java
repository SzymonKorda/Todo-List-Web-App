package com.szymon.todolist.service;

import com.szymon.todolist.exception.NotFoundException;
import com.szymon.todolist.model.Task;
import com.szymon.todolist.payload.TaskRequest;
import com.szymon.todolist.reposotiry.TaskRepository;
import com.szymon.todolist.security.User;
import com.szymon.todolist.security.UserDetailsImpl;
import com.szymon.todolist.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService{

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Task newTask(TaskRequest taskRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));
        Task task = new Task
                .Builder()
                .withTitle(taskRequest.getTitle())
                .withDescription(taskRequest.getDescription())
                .build();
        user.getTasks().add(task);
        task.setUser(user);
        userRepository.save(user);
        return task;
    }

    @Override
    public Task getTask(Integer id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));
        return taskRepository
                .findTaskByUserAndId(user, id)
                .orElseThrow(() -> new NotFoundException(String.format("Task with id %d not found", id)));
    }

    @Override
    public Task updateTask(Integer id, TaskRequest taskRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));
        Task task = taskRepository
                .findTaskByUserAndId(user, id)
                .orElseThrow(() -> new NotFoundException(String.format("Task with id %d not found", id)));

        if (taskRequest.getTitle() != null) {
            task.setTitle(taskRequest.getTitle());
        }
        if (taskRequest.getDescription() != null) {
            task.setDescription(taskRequest.getDescription());

        }
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Integer id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));
        Task task = taskRepository
                .findTaskByUserAndId(user, id)
                .orElseThrow(() -> new NotFoundException(String.format("Task with id %d not found", id)));
        taskRepository.delete(task);
    }

    @Override
    public List<Task> getUserTasks() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));
        return user.getTasks();
    }
}