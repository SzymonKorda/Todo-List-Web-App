package com.szymon.todolist.service;

import com.szymon.todolist.exception.NotFoundException;
import com.szymon.todolist.model.Task;
import com.szymon.todolist.payload.response.FullTaskResponse;
import com.szymon.todolist.payload.response.SimpleTaskResponse;
import com.szymon.todolist.payload.response.TaskCountResponse;
import com.szymon.todolist.payload.request.TaskRequest;
import com.szymon.todolist.reposotiry.TaskRepository;
import com.szymon.todolist.model.User;
import com.szymon.todolist.security.user.UserDetailsImpl;
import com.szymon.todolist.reposotiry.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    private static final String TASK_DESCRIPTION = "Description";
    private static final String TASK_TITLE = "Title";
    private static final String UPDATED_TASK_TITLE = "Updated title";
    private static final String UPDATED_TASK_DESCRIPTION = "Updated description";
    private static final String USER_NOT_FOUND_EXCEPTION_MESSAGE = "User with id 1 not found";
    private static final String TASK_NOT_FOUND_EXCEPTION_MESSAGE = "Task with id 1 not found";
    private static final Integer TASK_ID = 1;
    private static final Long USER_ID = 1L;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskServiceImpl(taskRepository, userRepository);
        mockSecurityContext();
    }

    @Test
    void shouldAddNewTask() {
        // given
        User user = prepareUser();
        TaskRequest request = prepareTaskRequest(TASK_TITLE, TASK_DESCRIPTION);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        //when
        taskService.newTask(request);
        //then
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowUserNotFoundException_addNewTask() {
        // given
        TaskRequest request = prepareTaskRequest(TASK_TITLE, TASK_DESCRIPTION);
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        //when
        Exception expected = null;
        try {
            taskService.newTask(request);
        } catch (Exception ex) {
            expected = ex;
        }
        //then
        verify(userRepository, times(0)).save(any());
        assertEquals(USER_NOT_FOUND_EXCEPTION_MESSAGE, expected.getMessage());
        assertTrue(expected instanceof NotFoundException);
    }

    @Test
    void shouldGetTask() {
        //given
        User user = prepareUser();
        Task task = prepareTask(user);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(taskRepository.findTaskByUserAndId(any(), any())).thenReturn(Optional.of(task));
        //when
        FullTaskResponse response = taskService.getTask(TASK_ID);
        //then
        assertNotNull(response);
        assertEquals(TASK_TITLE, response.getTitle());
        assertEquals(TASK_DESCRIPTION, response.getDescription());
        assertTrue(response.isActive());
    }

    @Test
    void shouldThrowTaskNotFoundException_shouldGetTask() {
        //given
        when(userRepository.findById(any())).thenReturn(Optional.of(prepareUser()));
        when(taskRepository.findTaskByUserAndId(any(), any())).thenReturn(Optional.empty());
        //when
        FullTaskResponse response = null;
        Exception expected = null;
        try {
            response = taskService.getTask(TASK_ID);
        } catch (Exception ex) {
            expected = ex;
        }
        //then
        assertNotNull(expected);
        assertEquals(TASK_NOT_FOUND_EXCEPTION_MESSAGE, expected.getMessage());
        assertTrue(expected instanceof NotFoundException);
    }

    @Test
    void updateTask() {
        //given
        User user = prepareUser();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(taskRepository.findTaskByUserAndId(any(), any())).thenReturn(Optional.of(prepareTask(user)));
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        //when
        taskService.updateTask(TASK_ID, prepareTaskRequest(UPDATED_TASK_TITLE, UPDATED_TASK_DESCRIPTION));
        //then
        verify(taskRepository, times(1)).save(captor.capture());
        Task updatedTask = captor.getValue();
        assertEquals(UPDATED_TASK_TITLE, updatedTask.getTitle());
        assertEquals(UPDATED_TASK_DESCRIPTION, updatedTask.getDescription());
    }

    @Test
    void deleteTask() {
        //given
        User user = prepareUser();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(taskRepository.findTaskByUserAndId(any(), any())).thenReturn(Optional.of(prepareTask(user)));
        //when
        taskService.deleteTask(TASK_ID);
        //then
        verify(taskRepository, times(1)).delete(any());
    }

    @Test
    void getUserTasks() {
        //given
        User user = prepareUser();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(taskRepository.findTaskByUserAndId(any(), any())).thenReturn(Optional.of(prepareTask(user)));
        //when
        List<FullTaskResponse> userTasks = taskService.getUserTasks();
        //then
        assertEquals(2, userTasks.size());
    }

    @Test
    void getUserActiveTasks() {
        //given
        User user = prepareUser();
        user.getTasks().get(0).setActive(false);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(taskRepository.findTaskByUserAndId(any(), any())).thenReturn(Optional.of(prepareTask(user)));
        //when
        List<SimpleTaskResponse> userTasks = taskService.getUserActiveTasks();
        //then
        assertEquals(1, userTasks.size());
    }

    @Test
    void getUserFinishedTasks() {
        //given
        User user = prepareUser();
        user.getTasks().get(0).setActive(false);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(taskRepository.findTaskByUserAndId(any(), any())).thenReturn(Optional.of(prepareTask(user)));
        //when
        List<FullTaskResponse> userTasks = taskService.getUserFinishedTasks();
        //then
        assertEquals(1, userTasks.size());
        assertFalse(userTasks.get(0).isActive());
    }

    @Test
    void finishTask() {
        //given
        User user = prepareUser();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(taskRepository.findTaskByUserAndId(any(), any())).thenReturn(Optional.of(prepareTask(user)));
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        //when
        taskService.finishTask(TASK_ID);
        //then
        verify(taskRepository, times(1)).save(captor.capture());
        assertFalse(captor.getValue().isActive());
    }

    @Test
    void getUserTaskCount() {
        //given
        User user = prepareUser();
        user.getTasks().get(0).setActive(false);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        //when
        TaskCountResponse userTaskCount = taskService.getUserTaskCount();
        //then
        assertEquals(1, userTaskCount.getActiveCount());
        assertEquals(1, userTaskCount.getFinishedCount());
    }

    private Task prepareTask(User user) {
        return Task.builder()
                .title(TASK_TITLE)
                .description(TASK_DESCRIPTION)
                .user(user)
                .build();
    }

    private TaskRequest prepareTaskRequest(String title, String description) {
        return TaskRequest.builder()
                .title(title)
                .description(description)
                .build();
    }

    private User prepareUser() {
        User user = User.builder()
                .id(USER_ID)
                .build();
        user.getTasks().addAll(List.of(prepareTask(user), prepareTask(user)));
        return user;
    }

    private void mockSecurityContext() {
        UserDetailsImpl userDetails = new UserDetailsImpl(USER_ID, "Username", "user@gmail.com", "password", new ArrayList<>());
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);
    }
}