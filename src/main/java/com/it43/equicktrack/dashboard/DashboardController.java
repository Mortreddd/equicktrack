package com.it43.equicktrack.dashboard;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.it43.equicktrack.dto.dashboard.ApprovedTransactionRequest;
import com.it43.equicktrack.dto.dashboard.DashboardDTO;
import com.it43.equicktrack.dto.dashboard.NotifyUserRequest;
import com.it43.equicktrack.dto.response.Response;
import com.it43.equicktrack.dto.transaction.TransactionDTO;
import com.it43.equicktrack.firebase.FirebaseMessagingService;
import com.it43.equicktrack.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final FirebaseMessagingService firebaseMessagingService;

    @GetMapping
    public ResponseEntity<DashboardDTO> getDashboardData() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(dashboardService.getDashboardData());
    }

    @GetMapping(path = "/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactions() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(dashboardService.getAllTransactions());
    }

    @PutMapping(path = "/transactions/{transactionId}/approved")
    public ResponseEntity<Response> approvedTransaction(
            @PathVariable("transactionId") Long transactionId,
            @RequestBody ApprovedTransactionRequest approvedTransactionRequest
    ) {
        dashboardService.approvedTransaction(transactionId, approvedTransactionRequest.getMessage());
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.builder()
                        .code(200)
                        .message("Successfully approved the transaction")
                        .build()
                );
    }

    @GetMapping(path = "/notify")
    public ResponseEntity<Response> notifyUser(@RequestBody NotifyUserRequest notifyUserRequest) throws FirebaseMessagingException {

        firebaseMessagingService.sendNotification(
                notifyUserRequest.getUserId(),
                notifyUserRequest.getTitle(),
                notifyUserRequest.getMessage()
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.builder()
                        .code(200)
                        .message("Notification has been sent")
                        .build()
                );
    }
}