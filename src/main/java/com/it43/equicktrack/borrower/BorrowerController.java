package com.it43.equicktrack.borrower;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(path="/api/v1/borrowers")
public class BorrowerController {
    private final BorrowerService borrowerService;

    @GetMapping
    public ResponseEntity<List<Borrower>> getBorrowers(){
        return ResponseEntity.ok().body(borrowerService.getBorrowers());
    }

    @GetMapping(path = "/{borrower_id}")

    public ResponseEntity<Borrower> getBorrowerById(@PathVariable("borrower_id") Long _id){
        Optional<Borrower> borrower = borrowerService.getBorrowerById(_id);

        return borrower.map(value ->
                ResponseEntity.ok().body(value)
        ).orElseGet(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        );
    }


}
