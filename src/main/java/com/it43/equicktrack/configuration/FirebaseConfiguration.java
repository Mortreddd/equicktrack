package com.it43.equicktrack.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class FirebaseConfiguration {

    private final Environment environment;
    private final ResourceLoader resourceLoader;

    @Value("${firebase.storage.bucket-url}")
    private String BUCKET_URL;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
//        InputStream fileInputStream = resourceLoader.getResource("classpath:firebase_credentials.json").getInputStream();
        InputStream fileInputStream = getFirebaseCredentialsStream();
        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(fileInputStream))
                .setStorageBucket(BUCKET_URL)
                .build();

        return FirebaseApp.initializeApp(firebaseOptions);
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        return FirebaseMessaging.getInstance(firebaseApp());
    }

    public InputStream getFirebaseCredentialsStream() throws IOException {
        String firebaseCredentials = environment.getProperty("firebase_credentials");
        String systemEnvironment = System.getenv("firebase_credentials");
        if(firebaseCredentials != null) {
            log.info("Successfully retrieve the firebase_credentials json");
            return new ByteArrayInputStream(firebaseCredentials.getBytes(StandardCharsets.UTF_8));
        } else if(systemEnvironment != null) {
            return new ByteArrayInputStream(systemEnvironment.getBytes(StandardCharsets.UTF_8));
        }

        log.error("Unable to load the firebase_credentials");
//        throw new FileNotFoundException("Firebase credentials file not found");
        return resourceLoader.getResource("classpath:firebase_credentials.json").getInputStream();
    }

//    public InputStream getFirebaseMessagingCredentialsStream() throws IOException {
//        String firebaseCredentials = environment.getProperty("firebase_messaging_credentials");
//        String systemEnvironment = System.getenv("firebase_messaging_credentials");
//        if(firebaseCredentials != null) {
//            return new ByteArrayInputStream(firebaseCredentials.getBytes(StandardCharsets.UTF_8));
//        } else if(systemEnvironment != null) {
//            return new ByteArrayInputStream(systemEnvironment.getBytes(StandardCharsets.UTF_8));
//        }
//
//        log.error("Unable to load the firebase_credentials");
////        throw new FileNotFoundException("Firebase credentials file not found");
//        return resourceLoader.getResource("classpath:google_services.json").getInputStream();
//    }
}
