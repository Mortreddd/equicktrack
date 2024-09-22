package com.it43.equicktrack.worker;


import com.it43.equicktrack.dto.transaction.TransactionDTO;
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
    private static final long TEST_TIME_CHECK = 5_000L;

    @Scheduled(fixedRate = TEST_TIME_CHECK)
//    @Scheduled(fixedRate = Constant.TIME_CHECK)
    public void checkLateReturnEquipments(){
        List<TransactionDTO> lateReturnees = transactionService.getTransactions()
                .stream()
                .filter(transaction -> {
                        return DateUtilities.isLate(transaction.getReturnDate()) && transaction.getReturnedAt() == null;
                })
                .toList();

        log.info("Executed at {}", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        // TODO : block code of push notification for the mobile application


    }


}
