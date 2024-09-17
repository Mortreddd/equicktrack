package com.it43.equicktrack.transaction;


import com.it43.equicktrack.dto.transaction.CreateTransactionRequest;
import com.it43.equicktrack.dto.transaction.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping(path = "/", consumes = {"application/json"})
    public ResponseEntity<List<Transaction>> getTransactions(){
        return ResponseEntity.ok().body(transactionService.getTransactions());
    }

//    This endpoint is for creating new transactions for equipments
    @PostMapping(path = "/borrow", consumes = {"application/json"})
    public ResponseEntity<Transaction> createBorrowTransaction(@Validated @RequestBody CreateTransactionRequest createTransactionRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transactionService.createTransaction(createTransactionRequestDTO));
    }

    @PatchMapping(path = "/{transactionId}/return")
    public ResponseEntity<TransactionDTO> createReturnTransaction(@PathVariable("transactionId") Long transactionId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                transactionService.createReturnTransaction(transactionId)
        );
    }

//    This endpoint is for updating the created transaction for the equipment
/*
    @PatchMapping(path = "/{transactionId}/return", consumes = {"application/json"})
    public ResponseEntity<Transaction> createReturnTransaction(@PathVariable("transactionId") Long transactionId) {

    }
*/
/*
    @PatchMapping(path = "/{transactionId}/update")
    public ResponseEntity<Transaction> updateTransaction(@RequestParam("transactionId") Long transactionId, @Validated @RequestBody ) {

    }
*/
    @DeleteMapping(path = "/{transactionId}/delete")
    public ResponseEntity<String> deleteTransaction(@RequestParam("transactionId") Long transactionId) {
        transactionService.deleteTransactionById(transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Transaction has been deleted");
    }



//
//    @GetMapping(path = "/borrower/{borrowerId}")
//    public ResponseEntity<BorrowerTransactionsDTO> getBorrowerTransactions(@PathVariable("borrowerId") Long id){
//        return ResponseEntity.ok()
//                .body(transactionService.)
//    }
}
