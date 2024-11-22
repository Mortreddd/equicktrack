package com.it43.equicktrack.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.user.User;
import com.it43.equicktrack.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FirebaseMessagingService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;

    public void sendNotification(Long userId, String title, String body) throws FirebaseMessagingException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message
                .builder()
                .setToken(user.getToken())
                .setNotification(notification)
                .build();

        firebaseMessaging.send(message);

    }


}
