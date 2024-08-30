package com.it43.equicktrack.users;

import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.user.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;

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
        Role roleSuperAdmin = Role.builder()
                .name(RoleName.SUPER_ADMIN)
                .build();

        Role roleAdmin = Role.builder()
                .name(RoleName.ADMIN)
                .build();

        Role roleProfessor = Role.builder()
                .name(RoleName.PROFESSOR)
                .build();

        Role roleStudent = Role.builder()
                .name(RoleName.STUDENT)
                .build();


        roleRepository.saveAll(List.of(roleAdmin, roleProfessor, roleStudent));
    }


    @Test
    @Transactional
    void testCanCreateUser() throws Exception{

        Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Admin role not found"));
        User userEmmanuel = User.builder()
                .fullName("Emmanuel")
                .roles(Set.of(adminRole))
                .email("emmanmale@gmail.com")
                .password("12345678")
                .build();

        userRepository.save(userEmmanuel);

        Optional<User> userFromDatabase = userRepository.findOne(Example.of(userEmmanuel));

        assertThat(userFromDatabase).isNotNull();
        assertThat(userFromDatabase.get().getFullName()).isEqualTo("Emmanuel");
        assertThat(userFromDatabase.get().getEmail()).isEqualTo("emmanmale@gmail.com");
        assertThat(userFromDatabase.isPresent()).isTrue();
    }

    @Test
    @Transactional
    void testStoreMultipleUsers() throws Exception {
        Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Admin role not found"));

        Role studentRole = roleRepository.findByName(RoleName.STUDENT)
                .orElseThrow(() -> new ResourceNotFoundException("Student role not found"));

        Role professorRole = roleRepository.findByName(RoleName.PROFESSOR)
                .orElseThrow(() -> new ResourceNotFoundException("Professor role not found"));


        List<User> users = List.of(
                User.builder()
                        .fullName("Emmanuel")
                        .roles(Set.of(adminRole))
                        .email("emmanmale@gmail.com")
                        .password("12345678")
                        .build(),

                User.builder()
                        .fullName("Shanks")
                        .roles(Set.of(professorRole))
                        .email("redhairshanks@gmail.com")
                        .password("1234567890")
                        .build(),

                User.builder()
                        .fullName("Luffy")
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


            assertThat(user.getFullName()).isEqualTo(userFromDatabase.getFullName());
            assertThat(user.getId()).isEqualTo(userFromDatabase.getId());
            assertThat(user.getEmail()).isEqualTo(userFromDatabase.getEmail());

        }
    }

    @Test
    @Transactional
    void testCanDeleteUser() throws Exception {
        Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Admin role not found"));

        Role studentRole = roleRepository.findByName(RoleName.STUDENT)
                .orElseThrow(() -> new ResourceNotFoundException("Student role not found"));

        Role professorRole = roleRepository.findByName(RoleName.PROFESSOR)
                .orElseThrow(() -> new ResourceNotFoundException("Professor role not found"));

        List<User> users = List.of(
                User.builder()
                        .fullName("Emmanuel")
                        .roles(Set.of(adminRole))
                        .email("emmanmale@gmail.com")
                        .password("12345678")
                        .build(),

                User.builder()
                        .fullName("Shanks")
                        .roles(Set.of(professorRole))
                        .email("redhairshanks@gmail.com")
                        .password("1234567890")
                        .build(),

                User.builder()
                        .fullName("Luffy")
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
        Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Admin role not found"));
        User user = User.builder()
                .id(1L)
                .fullName("Emmanuel")
                .roles(Set.of(adminRole))
                .email("emmanmale@gmail.com")
                .password("12345678")
                .build();


        userRepository.save(user);

        User updatedUser = User.builder()
                .id(1L)
                .fullName("Luffy")
                .roles(Set.of(adminRole))
                .email("emmanmale@gmail.com")
                .password("12345678")
                .build();


        userRepository.save(updatedUser);

        User userFromDatabase = userRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        assertThat(user.getFullName()).isNotEqualTo(userFromDatabase.getFullName());
        assertThat(user.getEmail()).isEqualTo(userFromDatabase.getEmail());
    }



}