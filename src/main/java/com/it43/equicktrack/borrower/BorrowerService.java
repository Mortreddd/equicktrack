package com.it43.equicktrack.borrower;

import com.it43.equicktrack.auth.JwtRegisterRequest;
import com.it43.equicktrack.dto.BorrowerTransactionsDTO;
import com.it43.equicktrack.exception.ResourceNotFoundException;
import com.it43.equicktrack.transaction.Transaction;
import com.it43.equicktrack.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TransactionRepository transactionRepository;

    public List<Borrower> getBorrowers(){
        return borrowerRepository
                .findAll();
    }

    public Optional<Borrower> getBorrowerById(Long id){
        return borrowerRepository.findById(id);
    }

    public Borrower createNewBorrower(JwtRegisterRequest _borrower){
        Role borrowerRole = roleRepository.findById(_borrower.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role user not found"));

        Borrower borrower = Borrower.builder()
                .firstName(_borrower.getFirstName())
                .lastName(_borrower.getLastName())
                .email(_borrower.getEmail())
                .roles(Set.of(borrowerRole))
                .password(passwordEncoder.encode(_borrower.getPassword()))
                .emailVerifiedAt(null)
                .createdAt(LocalDateTime.now())
                .build();

        borrowerRepository.save(borrower);
        return borrower;
    }

    public Borrower deleteBorrowerById(Long _id){
        Borrower borrower = borrowerRepository.findById(_id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        borrowerRepository.delete(borrower);
        return borrower;
    }


    public List<Borrower> saveBorrowers(List<Borrower> borrowers){
        borrowerRepository.saveAll(borrowers);
        return borrowers;
    }

    public Borrower updateBorrower(Borrower _borrower){
        borrowerRepository.save(_borrower);
        return _borrower;
    }
    public BorrowerTransactionsDTO getBorrowerTransactionsById(Long _id){
        Borrower borrower = borrowerRepository.findById(_id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower does not exists"));
        List<Transaction> transactions = transactionRepository.findTransactionsByBorrower(borrower)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower doesn't have transactions history"));
        return new BorrowerTransactionsDTO(borrower, transactions);
    }

}
