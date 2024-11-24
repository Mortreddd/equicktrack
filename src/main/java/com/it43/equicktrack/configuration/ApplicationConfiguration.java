package com.it43.equicktrack.configuration;

import com.it43.equicktrack.user.*;
import com.it43.equicktrack.util.DateUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationConfiguration {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService(){
        return username ->
                userRepository.findByEmail(username)
                        .orElseThrow(() ->
                                new UsernameNotFoundException("Username not found")
                        );
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }


    @Bean
    CommandLineRunner roleInit(){
        return args -> {
            roleRepository.saveIfNotExists(Role.builder()
                    .name(RoleName.SUPER_ADMIN)
                    .build());
            roleRepository.saveIfNotExists(Role.builder()
                    .name(RoleName.ADMIN)
                    .build());
            roleRepository.saveIfNotExists(Role.builder()
                    .name(RoleName.PROFESSOR)
                    .build());
            roleRepository.saveIfNotExists(Role.builder()
                    .name(RoleName.STUDENT)
                    .build());
        };
    }

    @Bean
    CommandLineRunner superAdminInit() {
        return args -> {
            roleInit();

            Role superAdminRole = roleRepository.findByName(RoleName.SUPER_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role is not yet created"));


            List<User> users = userRepository.findAll();
            boolean hasSuperAdmin = users.stream()
                    .anyMatch((u) -> u.getRoles()
                            .stream()
                            .anyMatch( (r) -> Objects.equals(r, superAdminRole))
                    );


            if(!hasSuperAdmin) {
                User superAdmin = User.builder()
                        .fullName("Emmanuel Male")
                        .photoUrl(null)
                        .googleUid(null)
                        .email("emmanmale@gmail.com")
                        .roles(Set.of(superAdminRole))
                        .contactNumber("09670778658")
                        .password(new BCryptPasswordEncoder().encode("12345678"))
                        .emailVerifiedAt(DateUtilities.now())
                        .contactNumberVerifiedAt(DateUtilities.now())
                        .createdAt(DateUtilities.now())
                        .build();

                userRepository.save(superAdmin);
            }
            Role studentRole = roleRepository.findByName(RoleName.STUDENT)
                    .orElseThrow(() -> new RuntimeException("Student role is not yet created"));
            List<User> userStudents = List.of(
                    User.builder()
                            .fullName("Roselle Manalansan")
                            .photoUrl(null)
                            .googleUid(null)
                            .email("roselleannmanalansan@gmail.com")
                            .roles(Set.of(studentRole))
                            .contactNumber("09673483180")
                            .password(new BCryptPasswordEncoder().encode("12345678"))
                            .emailVerifiedAt(DateUtilities.now())
                            .contactNumberVerifiedAt(DateUtilities.now())
                            .createdAt(DateUtilities.now())
                            .build(),
                    User.builder()
                            .fullName("Bea Mangulabnan")
                            .photoUrl(null)
                            .googleUid(null)
                            .email("beamangulabnan05@gmail.com")
                            .roles(Set.of(studentRole))
                            .contactNumber("09557069891")
                            .password(new BCryptPasswordEncoder().encode("12345678"))
                            .emailVerifiedAt(DateUtilities.now())
                            .contactNumberVerifiedAt(DateUtilities.now())
                            .createdAt(DateUtilities.now())
                            .build(),
                    User.builder()
                            .fullName("Jenny Macoto")
                            .photoUrl(null)
                            .googleUid(null)
                            .email("jaexdbusiness@gmail.com")
                            .roles(Set.of(studentRole))
                            .contactNumber("09158808973")
                            .password(new BCryptPasswordEncoder().encode("12345678"))
                            .emailVerifiedAt(DateUtilities.now())
                            .contactNumberVerifiedAt(DateUtilities.now())
                            .createdAt(DateUtilities.now())
                            .build(),
                    User.builder()
                            .fullName("Ella Macaspac")
                            .photoUrl(null)
                            .googleUid(null)
                            .email("ellamacaspac81@gmail.com")
                            .roles(Set.of(studentRole))
                            .contactNumber("09916830595")
                            .password(new BCryptPasswordEncoder().encode("12345678"))
                            .emailVerifiedAt(DateUtilities.now())
                            .contactNumberVerifiedAt(DateUtilities.now())
                            .createdAt(DateUtilities.now())
                            .build(),
                    User.builder()
                            .fullName("Harley Mallari")
                            .photoUrl(null)
                            .googleUid(null)
                            .email("harleymallari3@gmail.com")
                            .roles(Set.of(studentRole))
                            .contactNumber("09503862258")
                            .password(new BCryptPasswordEncoder().encode("12345678"))
                            .emailVerifiedAt(DateUtilities.now())
                            .contactNumberVerifiedAt(DateUtilities.now())
                            .createdAt(DateUtilities.now())
                            .build()
            );
            List<String> existingStudentEmails = userRepository.findAll()
                    .stream()
                    .map(User::getEmail)
                    .toList();
            List<User> missingStudents = userStudents.stream()
                    .filter(student -> !existingStudentEmails.contains(student.getEmail()))
                    .toList();

            userRepository.saveAll(missingStudents);
        };
    }

}
