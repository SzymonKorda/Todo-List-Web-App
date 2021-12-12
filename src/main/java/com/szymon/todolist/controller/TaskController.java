package com.szymon.todolist.controller;

import com.szymon.todolist.payload.SimpleTaskResponse;
import com.szymon.todolist.payload.TaskRequest;
import com.szymon.todolist.payload.FullTaskResponse;
import com.szymon.todolist.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
@Api(tags = "Tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @ApiOperation(value = "This method is used to create a new task.")
    @PostMapping("/task")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskRequest taskRequest) {
        taskService.newTask(taskRequest);
        return new ResponseEntity<>("Task created successfully!", HttpStatus.CREATED);
    }

    @ApiOperation(value = "This method is used to get full task information.")
    @GetMapping("/task/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getTask(@PathVariable Integer id) {
        FullTaskResponse task = taskService.getTask(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @ApiOperation(value = "This method is used to get all user's tasks.")
    @GetMapping("/task")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserTasks() {
        List<FullTaskResponse> tasks = taskService.getUserTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @ApiOperation(value = "This method is used to get user's active tasks.")
    @GetMapping("/task/active")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserActiveTasks() {
        List<SimpleTaskResponse> tasks = taskService.getUserActiveTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @ApiOperation(value = "This method is used to get user's finished tasks.")
    @GetMapping("/task/finish")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserFinishedTasks() {
        List<FullTaskResponse> tasks = taskService.getUserFinishedTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @ApiOperation(value = "This method is used to update task.")
    @PutMapping("/task/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateTask(@PathVariable Integer id, @Valid @RequestBody TaskRequest taskRequest) {
        taskService.updateTask(id, taskRequest);
        return new ResponseEntity<>("Task updated successfully!", HttpStatus.OK);
    }

    @ApiOperation(value = "This method is used to delete task.")
    @DeleteMapping("/task/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteTask(@PathVariable Integer id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>("Task deleted successfully!", HttpStatus.OK);
    }

    @ApiOperation(value = "This method is used to finish task.")
    @PostMapping("/task/{id}/finish")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> finishTask(@PathVariable Integer id) {
        taskService.finishTask(id);
        return new ResponseEntity<>("Task finished successfully!", HttpStatus.CREATED);
    }

}

