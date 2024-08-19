package com.it43.equicktrack.users;

import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() throws Exception {
        Role roleAdmin = Role.builder()
                .id(1)
                .name(RoleName.ROLE_ADMIN)
                .build();

        Role roleProfessor = Role.builder()
                .id(2)
                .name(RoleName.ROLE_PROFESSOR)
                .build();

        Role roleStudent = Role.builder()
                .id(3)
                .name(RoleName.ROLE_STUDENT)
                .build();


        roleRepository.saveAll(List.of(roleAdmin, roleProfessor, roleStudent));
    }

    @Test
    void testCanCreateUser() throws Exception{

        Role adminRole = roleRepository.findById(1)
                .orElseThrow(() -> new ResourceNotFoundException("Admin role not found"));
        User userEmmanuel = User.builder()
                .id(1L)
                .firstName("Emmanuel")
                .lastName("Male")
                .roles(Set.of(adminRole))
                .email("emmanmale@gmail.com")
                .password("12345678")
                .build();

        userRepository.save(userEmmanuel);

        assertThat(userEmmanuel).isNotNull();
        assertThat(userEmmanuel.getFirstName()).isEqualTo("Emmanuel");
        assertThat(userEmmanuel.getPassword()).isNotEqualTo("1234567");


    }

}
