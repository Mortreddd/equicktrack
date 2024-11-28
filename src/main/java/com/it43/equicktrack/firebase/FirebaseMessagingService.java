package com.it43.equicktrack.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.notification.NotificationRepository;
import com.it43.equicktrack.notification.NotificationService;
import com.it43.equicktrack.transaction.Transaction;
import com.it43.equicktrack.transaction.TransactionRepository;
import com.it43.equicktrack.user.User;
import com.it43.equicktrack.user.UserRepository;
import com.it43.equicktrack.util.DateUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.it43.equicktrack.notification.Notification;

import java.util.List;

@Service
@Slf4j
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

    public void sendMultipleNotifications(List<Long> transactionIds, String title, String body) throws FirebaseMessagingException {
        List<Transaction> transactions = transactionRepository.findAllById(transactionIds)
                .stream()
                .peek(transaction -> transaction.setNotifiedAt(DateUtilities.now()))
                .toList();

        List<User> users = transactions.stream()
                .map(Transaction::getUser)
                .toList();

        List<String> tokens = users.stream()
                .map(User::getToken)
                .filter(token -> token != null && !token.isEmpty())
                .toList();

        log.info("Tokens to notify: {}", tokens);

        if (tokens.isEmpty()) {
            log.warn("No valid tokens found for the given transactions.");
            return;
        }

        com.google.firebase.messaging.Notification notification = com.google.firebase.messaging.Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        MulticastMessage multicastMessage = MulticastMessage.builder()
                .addAllTokens(tokens)
                .setNotification(notification)
                .build();

        firebaseMessaging.sendEachForMulticast(multicastMessage);
        transactionRepository.saveAll(transactions);
    }


}
