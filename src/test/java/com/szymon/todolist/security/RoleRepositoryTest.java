package com.szymon.todolist.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findByName() {
        // given
        // when
        Optional<Role> role = roleRepository.findByName(ERole.ROLE_USER);
        // then
        assertEquals(ERole.ROLE_USER, role.get().getName());
    }
}