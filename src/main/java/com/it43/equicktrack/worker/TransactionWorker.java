package com.it43.equicktrack.worker;


import com.google.firebase.messaging.FirebaseMessagingException;
import com.it43.equicktrack.dto.transaction.TransactionDTO;
import com.it43.equicktrack.notification.NotificationService;
import com.it43.equicktrack.transaction.Transaction;
import com.it43.equicktrack.transaction.TransactionRepository;
import com.it43.equicktrack.transaction.TransactionService;
import com.it43.equicktrack.util.Constant;
import com.it43.equicktrack.util.DateUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionWorker {

    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;

    @Scheduled(fixedRate = Constant.TIME_CHECK)
    public void checkLateReturnEquipments() throws FirebaseMessagingException {
        String titleMessage = "Notice";
        String message = String.format(Constant.RETURN_NOTIFICATION_MESSAGE, "This is a reminder for the equipment you borrowed.");
        List<Transaction> lateReturnees = transactionRepository.findAll()
                .stream()
                .filter(transaction ->
                        DateUtilities.isEnding(transaction.getReturnDate()) &&
                        transaction.getReturnedAt() == null &&
                        transaction.getNotifiedAt() == null
                ).toList();

        List<Long> transactionIds = lateReturnees.stream()
                        .map(Transaction::getId)
                        .toList();

        notificationService.notifyUsers(transactionIds, titleMessage, message);

    }


}
