package com.it43.equicktrack.worker;


import com.it43.equicktrack.transaction.Transaction;
import com.it43.equicktrack.transaction.TransactionService;
import com.it43.equicktrack.util.Constant;
import com.it43.equicktrack.util.DateUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionWorker {
    private final TransactionService transactionService;
//    @Scheduled(fixedRate = Constant.TIME_CHECK)
    public void checkLateReturnEquipments(){
        List<Transaction> lateReturnees = transactionService.getTransactions()
                .stream()
                .filter(transaction ->
                        DateUtilities.isLate(transaction.getReturnDate())
                )
                .toList();

        log.info("Executed at {}", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        // TODO : block code of push notification for the mobile application
    }


}
