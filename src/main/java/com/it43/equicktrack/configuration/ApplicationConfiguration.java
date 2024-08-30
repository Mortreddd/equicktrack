package com.it43.equicktrack.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.it43.equicktrack.firebase.FirebaseService;
import com.it43.equicktrack.user.UserRepository;
import com.it43.equicktrack.user.Role;
import com.it43.equicktrack.user.RoleName;
import com.it43.equicktrack.user.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationConfiguration {

    private final UserRepository userRepository;
    private final ResourceLoader resourceLoader;
    private final FirebaseService firebaseService;

    @Value("${firebase.storage.bucket-url}")
    private String BUCKET_URL;

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
    public FirebaseApp firebaseApp() throws IOException {
        InputStream fileInputStream = resourceLoader.getResource("classpath:equicktrack-api-service-firebase-adminsdk.json").getInputStream();
        log.info("Bucket url: {}", BUCKET_URL);
        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(fileInputStream))
                .setStorageBucket(BUCKET_URL)
                .build();


        return FirebaseApp.initializeApp(firebaseOptions);
    }


    @Bean
    CommandLineRunner init(RoleRepository roleRepository){
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
}
