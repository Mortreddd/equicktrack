package com.it43.equicktrack.worker;


import com.it43.equicktrack.transaction.Transaction;
import com.it43.equicktrack.transaction.TransactionService;
import com.it43.equicktrack.util.DateUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionWorker {
    private final TransactionService transactionService;
    // 30 seconds
    private static final int TIME_CHECK = 30_000;
    // 15 minutes
    private static final int LATE_RETURN_MINUTES = 900_000;

    @Scheduled(fixedRate = TIME_CHECK)
    public void checkLateReturnEquipments(){
        List<Transaction> lateReturnees = transactionService.getTransactions()
                .stream()
                .filter(transaction ->
                        DateUtilities.isLate(transaction.getReturnDate())
                )
                .toList();
        
        // TODO : block code of push notification for the mobile application
    }


}
