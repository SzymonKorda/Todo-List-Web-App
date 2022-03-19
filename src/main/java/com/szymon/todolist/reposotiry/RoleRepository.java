package com.szymon.todolist.reposotiry;

import com.szymon.todolist.model.Role;
import com.szymon.todolist.security.user.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}