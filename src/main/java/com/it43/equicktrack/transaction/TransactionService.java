package com.it43.equicktrack.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<Transaction> getTransactions(){
        return transactionRepository.findAll();
    }

//    public List<Transaction> getOnUseTransactions(){
//        return transactionRepository.findAll()
//                .stream()
//                .filter(_transaction ->
//                        _transaction.getBorrowDate()
//                                .toInstant()
//                                .atZone(ZoneId.systemDefault())
//                                .toLocalDateTime()
//                                .isBefore(LocalDateTime.now()))
//                .collect(Collectors.toList());
//    }


}
