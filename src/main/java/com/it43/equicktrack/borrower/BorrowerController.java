package com.it43.equicktrack.borrower;

import com.it43.equicktrack.dto.BorrowerTransactionsDTO;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.util.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(path="/api/v1/borrowers")
public class BorrowerController {
    private final BorrowerService borrowerService;
    private final AuthenticatedUser authenticatedUser;
    @GetMapping
    public ResponseEntity<List<Borrower>> getBorrowers(){
        return ResponseEntity.ok().body(borrowerService.getBorrowers());
    }

    @GetMapping(path = "/{borrower_id}")

    public ResponseEntity<Borrower> getBorrowerById(@PathVariable("borrower_id") Long _id){
        Borrower borrower = borrowerService.getBorrowerById(_id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return ResponseEntity.ok().body(borrower);
    }

    @DeleteMapping(path = "/{borrower_id}/delete")
    public ResponseEntity<Borrower> deleteBorrowerById(@PathVariable("borrower_id") Long _id){
        return ResponseEntity.ok().body(borrowerService.deleteBorrowerById(_id));
    }
//    "borrower" : {
//    transactions : [
//      "borrower_id" : 1
//      "equipment_id" : int
//      ]
//    }
    @GetMapping(path = "/{borrowerId}/transactions")
    public ResponseEntity<BorrowerTransactionsDTO> getBorrowerTransactions(@PathVariable("borrowerId") Long _id){
        return ResponseEntity.ok()
                .body(borrowerService.getBorrowerTransactionsById(_id));
    }

    @PostMapping(path = "/me")
    public ResponseEntity<Authentication> getAuthenticatedUser(){
        return ResponseEntity.ok().body(AuthenticatedUser.getAuthenticatedUser());
    }

}
