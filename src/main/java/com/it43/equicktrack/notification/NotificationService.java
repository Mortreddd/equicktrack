package com.it43.equicktrack.notification;

import com.it43.equicktrack.contact.ContactService;
import com.it43.equicktrack.dto.notification.NotificationDTO;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.transaction.Transaction;
import com.it43.equicktrack.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final TransactionRepository transactionRepository;
    private final ContactService contactService;

    public List<NotificationDTO> getNotifications(Long userId) {
        return notificationRepository.findAll()
                .stream()
                .filter(notif -> Objects.equals(notif.getUser().getId(), userId) )
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
    public void notifyUser(Long transactionId, String title, String message) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("The transaction can't be found"));

        String formattedMessage = String.format("%s, %s", title, message);
        String phoneNumber = transaction.getUser().getContactNumber();
//        contactService.notifyUser(phoneNumber, formattedMessage);
    }
}
