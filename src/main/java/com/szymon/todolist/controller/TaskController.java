package com.szymon.todolist.controller;

import com.szymon.todolist.model.Task;
import com.szymon.todolist.payload.TaskRequest;
import com.szymon.todolist.payload.TaskResponse;
import com.szymon.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/task")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createTask(@RequestBody TaskRequest taskRequest) {
        taskService.newTask(taskRequest);
        return new ResponseEntity<>("Task created successfully!", HttpStatus.CREATED);
    }

    @GetMapping("/task/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getTask(@PathVariable Integer id) {
        TaskResponse response = taskService.getTask(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/task/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateTask(@PathVariable Integer id, @RequestBody TaskRequest taskRequest) {
        taskService.updateTask(id, taskRequest);
        return new ResponseEntity<>("Task updated successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/task/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteTask(@PathVariable Integer id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>("Task deleted successfully!", HttpStatus.OK);
    }

    @GetMapping("/task")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getTasks() {
        List<Task> tasks = taskService.getUserTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping("/task/{id}/finish")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> finishTask(@PathVariable Integer id) {
        taskService.finishTask(id);
        return new ResponseEntity<>("Task finished successfully!", HttpStatus.CREATED);
    }

}
