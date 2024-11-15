package com.it43.equicktrack.notification;

import com.it43.equicktrack.contact.ContactService;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.transaction.Transaction;
import com.it43.equicktrack.transaction.TransactionRepository;
import com.it43.equicktrack.user.User;
import com.it43.equicktrack.util.DateUtilities;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private NotificationRepository notificationRepository;
    private TransactionRepository transactionRepository;
    private ContactService contactService;

    public void notifyUser(Long transactionId, String message) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("The transaction can't be found"));

        notificationRepository.save(Notification.builder()
                .user(transaction.getUser())
                .message(message)
                .createdAt(DateUtilities.now())
                .updatedAt(DateUtilities.now())
                .build()
        );

        String phoneNumber = transaction.getUser().getContactNumber();
        contactService.notifyUser(phoneNumber, message);
    }
}
