package com.it43.equicktrack.transaction;


import com.it43.equicktrack.dto.BorrowerTransactionsDTO;
import com.it43.equicktrack.dto.transaction.CreateTransactionRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(){
        return ResponseEntity.ok().body(transactionService.getTransactions());
    }

    @PostMapping(path = "/create")
    public ResponseEntity<Transaction> createTransaction(@ModelAttribute CreateTransactionRequestDTO createTransactionRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.createTransaction(createTransactionRequestDTO));
    }

//
//    @GetMapping(path = "/borrower/{borrowerId}")
//    public ResponseEntity<BorrowerTransactionsDTO> getBorrowerTransactions(@PathVariable("borrowerId") Long id){
//        return ResponseEntity.ok()
//                .body(transactionService.)
//    }
}
