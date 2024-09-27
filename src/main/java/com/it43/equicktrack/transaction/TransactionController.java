package com.it43.equicktrack.transaction;


import com.it43.equicktrack.dto.transaction.CreateReturnTransactionRequest;
import com.it43.equicktrack.dto.transaction.CreateTransactionRequest;
import com.it43.equicktrack.dto.transaction.TransactionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getTransactions() {
        return ResponseEntity.ok().body(transactionService.getTransactions());
    }
    //    This endpoint is for creating new transactions for equipments
    @PostMapping(path = "/borrow", consumes = {"application/json"})
    public ResponseEntity<Transaction> createBorrowTransaction(@Validated @RequestBody CreateTransactionRequest createTransactionRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(transactionService.createTransaction(createTransactionRequestDTO));
    }

    @PatchMapping(path = "/return", consumes = {"application/json", "multipart/form-data"})
    public ResponseEntity<TransactionDTO> createReturnTransaction(
            @Validated @ModelAttribute CreateReturnTransactionRequest createReturnTransactionRequest
    ) throws IOException {

        return ResponseEntity.status(HttpStatus.OK).body(
                transactionService.createReturnTransaction(createReturnTransactionRequest)
        );
    }

    @DeleteMapping(path = "/{transactionId}/delete")
    public ResponseEntity<String> deleteTransaction(@RequestParam("transactionId") Long transactionId) {
        transactionService.deleteTransactionById(transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Transaction has been deleted");
    }

}

