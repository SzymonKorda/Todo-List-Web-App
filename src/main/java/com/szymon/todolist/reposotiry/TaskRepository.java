package com.szymon.todolist.reposotiry;

import com.szymon.todolist.model.Task;
import com.szymon.todolist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findTaskByUserAndId(User user, Integer id);
}
