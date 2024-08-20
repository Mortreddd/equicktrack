package com.it43.equicktrack.users;

import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.user.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
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
                .name(RoleName.ROLE_ADMIN)
                .build();

        Role roleProfessor = Role.builder()
                .name(RoleName.ROLE_PROFESSOR)
                .build();

        Role roleStudent = Role.builder()
                .name(RoleName.ROLE_STUDENT)
                .build();


        roleRepository.saveAll(List.of(roleAdmin, roleProfessor, roleStudent));
    }


    @Test
    @Transactional
    void testCanCreateUser() throws Exception{

        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Admin role not found"));
        User userEmmanuel = User.builder()
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

    @Test
    @Transactional
    void testStoreMultipleUsers() throws Exception {
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Admin role not found"));

        Role studentRole = roleRepository.findByName(RoleName.ROLE_STUDENT)
                .orElseThrow(() -> new ResourceNotFoundException("Student role not found"));

        Role professorRole = roleRepository.findByName(RoleName.ROLE_PROFESSOR)
                .orElseThrow(() -> new ResourceNotFoundException("Professor role not found"));


        List<User> users = List.of(
                User.builder()
                        .firstName("Emmanuel")
                        .lastName("Male")
                        .roles(Set.of(adminRole))
                        .email("emmanmale@gmail.com")
                        .password("12345678")
                        .build(),

                User.builder()
                        .firstName("Red Hair")
                        .lastName("Shanks")
                        .roles(Set.of(professorRole))
                        .email("redhairshanks@gmail.com")
                        .password("1234567890")
                        .build(),

                User.builder()
                        .firstName("Monkey D")
                        .lastName("Luffy")
                        .roles(Set.of(studentRole))
                        .email("luffy@gmail.com")
                        .password("123456789")
                        .build()
                );


        userRepository.saveAll(users);

        List<User> usersFromDatabase = userRepository.findAll();

        final int USERS_SIZE = Math.max(usersFromDatabase.size(), users.size());

        for(int index = 0; index < USERS_SIZE; index++){
            var user = users.get(index);
            var userFromDatabase = usersFromDatabase.get(index);


            assertThat(user.getFirstName()).isEqualTo(userFromDatabase.getFirstName());
            assertThat(user.getLastName()).isEqualTo(userFromDatabase.getLastName());
            assertThat(user.getId()).isEqualTo(userFromDatabase.getId());
            assertThat(user.getEmail()).isEqualTo(userFromDatabase.getEmail());

        }
    }

    @Test
    @Transactional
    void testCanDeleteUser() throws Exception {
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Admin role not found"));

        Role studentRole = roleRepository.findByName(RoleName.ROLE_STUDENT)
                .orElseThrow(() -> new ResourceNotFoundException("Student role not found"));

        Role professorRole = roleRepository.findByName(RoleName.ROLE_PROFESSOR)
                .orElseThrow(() -> new ResourceNotFoundException("Professor role not found"));

        List<User> users = List.of(
                User.builder()
                        .firstName("Emmanuel")
                        .lastName("Male")
                        .roles(Set.of(adminRole))
                        .email("emmanmale@gmail.com")
                        .password("12345678")
                        .build(),

                User.builder()
                        .firstName("Red Hair")
                        .lastName("Shanks")
                        .roles(Set.of(professorRole))
                        .email("redhairshanks@gmail.com")
                        .password("1234567890")
                        .build(),

                User.builder()
                        .firstName("Monkey D")
                        .lastName("Luffy")
                        .roles(Set.of(studentRole))
                        .email("luffy@gmail.com")
                        .password("123456789")
                        .build()
        );


        userRepository.saveAll(users);

        userRepository.deleteById(2L);

        List<User> usersFromDatabase = userRepository.findAll();
        Optional<User> deletedUser = userRepository.findById(2L);
        assertThat(usersFromDatabase.stream()
                .anyMatch(_u -> _u.getId() != 2L))
                .isTrue();

        assertThat(deletedUser.isEmpty()).isTrue();
    }


    @Test
    @Transactional
    void testCanUpdateUser() throws Exception {
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Admin role not found"));
        User user = User.builder()
                .id(1L)
                .firstName("Emmanuel")
                .lastName("Male")
                .roles(Set.of(adminRole))
                .email("emmanmale@gmail.com")
                .password("12345678")
                .build();


        userRepository.save(user);

        User updatedUser = User.builder()
                .id(1L)
                .firstName("Monkey D.")
                .lastName("Luffy")
                .roles(Set.of(adminRole))
                .email("emmanmale@gmail.com")
                .password("12345678")
                .build();


        userRepository.save(updatedUser);

        User userFromDatabase = userRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        assertThat(user.getFirstName()).isNotEqualTo(userFromDatabase.getFirstName());
        assertThat(user.getEmail()).isEqualTo(userFromDatabase.getEmail());
    }



}