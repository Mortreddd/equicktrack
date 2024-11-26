package com.it43.equicktrack.transaction;


import com.it43.equicktrack.dto.response.Response;
import com.it43.equicktrack.dto.transaction.*;
import com.it43.equicktrack.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    private final NotificationService notificationService;
    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<Page<TransactionDTO>> getTransactions(
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.ok().body(transactionService.getTransactions(pageNo, pageSize));
    }
    //    This endpoint is for creating new transactions for equipments
    @PostMapping(path = "/borrow", consumes = {"application/json"})
    public ResponseEntity<TransactionDTO> createBorrowTransaction(@Validated @RequestBody CreateTransactionRequest createTransactionRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(transactionService.createTransaction(createTransactionRequestDTO));
    }

    @PatchMapping(path = "/return", consumes = {"multipart/form-data"})
    public ResponseEntity<TransactionDTO> createReturnTransaction(
            @Validated @ModelAttribute CreateReturnTransactionRequest createReturnTransactionRequest
    ) throws IOException {

        return ResponseEntity.status(HttpStatus.OK).body(
                transactionService.createReturnTransaction(createReturnTransactionRequest)
        );
    }

    @DeleteMapping(path = "/{transactionId}/delete")
    public ResponseEntity<Response> deleteTransaction(@PathVariable("transactionId") Long transactionId) {
        transactionService.deleteTransactionById(transactionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.builder()
                        .code(200)
                        .message("Successfully deleted the transactions")
                        .build()
                );
    }

    @GetMapping(path = "/occupied")
    public ResponseEntity<List<TransactionDTO>> getOnUsedEquipment() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(transactionService.getOnUsedEquipments());
    }

    @PostMapping(path = "/{transactionId}/notify")
    public ResponseEntity<Response> notifyUser(@PathVariable("transactionId") Long transactionId, @RequestBody SendNotificationRequest sendNotificationRequest) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.builder()
                        .code(200)
                        .message("Successfully notified the user")
                        .build()

                );
    }

    @PutMapping(path = "/{transactionId}/approve")
    public ResponseEntity<TransactionDTO> approvedReturnTransaction(
            @PathVariable("transactionId") Long transactionId,
            @RequestBody ApproveReturnRequest approveReturnRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(transactionService.approveReturn(transactionId, approveReturnRequest.getMessage()));
    }



}

