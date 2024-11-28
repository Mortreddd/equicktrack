package com.it43.equicktrack.notification;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.it43.equicktrack.dto.notification.NotificationDTO;
import com.it43.equicktrack.firebase.FirebaseMessagingService;
import com.it43.equicktrack.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final TransactionRepository transactionRepository;
    private final FirebaseMessagingService firebaseMessagingService;

    public List<NotificationDTO> getNotifications(Long userId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        return notificationRepository.findAll(sort)
                .stream()
                .filter(notif -> Objects.equals(notif.getUser().getId(), userId))
                .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
                .map(notif -> NotificationDTO.builder()
                        .id(notif.getId())
                        .title(notif.getTitle())
                        .message(notif.getMessage())
                        .createdAt(notif.getCreatedAt())
                        .updatedAt(notif.getUpdatedAt())
                        .readAt(notif.getReadAt())
                        .receivedAt(notif.getReceivedAt())
                        .build()
                )
                .toList();
    }

    public void notifyUser(Long transactionId, String body, String title) throws FirebaseMessagingException {

        firebaseMessagingService.sendNotification(
                transactionId,
                title,
                body
        );
    }

    public void notifyUser(Long transactionId, String body) throws FirebaseMessagingException {
        String title = "You were notified";
        firebaseMessagingService.sendNotification(transactionId, title, body);
    }

    public void notifyUsers(List<Long> transactionsIds, String title, String body) throws FirebaseMessagingException{
        firebaseMessagingService.sendMultipleNotifications(transactionsIds, title, body);
    }


}
