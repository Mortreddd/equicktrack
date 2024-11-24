package com.it43.equicktrack.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.notification.NotificationRepository;
import com.it43.equicktrack.notification.NotificationService;
import com.it43.equicktrack.transaction.Transaction;
import com.it43.equicktrack.transaction.TransactionRepository;
import com.it43.equicktrack.user.UserRepository;
import com.it43.equicktrack.util.DateUtilities;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.it43.equicktrack.notification.Notification;

@Service
@RequiredArgsConstructor
public class FirebaseMessagingService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationRepository notificationRepository;

    public void sendNotification(Long transactionId, String title, String body) throws FirebaseMessagingException {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        com.google.firebase.messaging.Notification notification = com.google.firebase.messaging.Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message
                .builder()
                .setToken(transaction.getUser().getToken())
                .setNotification(notification)
                .build();

        Notification userNotification = Notification.builder()
                .title(title)
                .message(body)
                .user(transaction.getUser())
                .createdAt(DateUtilities.now())
                .receivedAt(DateUtilities.now())
                .build();

        notificationRepository.save(userNotification);
        firebaseMessaging.send(message);

    }


}
