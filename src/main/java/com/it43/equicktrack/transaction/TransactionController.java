package com.it43.equicktrack.transaction;


import com.it43.equicktrack.dto.BorrowerTransactionsDTO;
import lombok.RequiredArgsConstructor;
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
//
//    @GetMapping(path = "/borrower/{borrowerId}")
//    public ResponseEntity<BorrowerTransactionsDTO> getBorrowerTransactions(@PathVariable("borrowerId") Long id){
//        return ResponseEntity.ok()
//                .body(transactionService.)
//    }
}
