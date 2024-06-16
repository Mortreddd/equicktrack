package com.it43.equicktrack.borrower;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public List<Borrower> getBorrowers(){
        return borrowerRepository.findAll();
    }

    public Optional<Borrower> getBorrowerById(Long id){
        return borrowerRepository.findById(id);
    }

    // * Forced to use the request as parameter since it has role name in request body
    public Borrower createNewBorrower(Borrower _borrower){
        Borrower borrower = Borrower.builder()
                .firstName(_borrower.getFirstName())
                .lastName(_borrower.getLastName())
                .email(_borrower.getEmail())
                .roles(_borrower.getRoles())
                .password(passwordEncoder.encode(_borrower.getPassword()))
                .createdAt(Timestamp.from(Instant.now()))
                .build();
        borrowerRepository.save(borrower);

        return borrower;
    }

    public void deleteBorrowerById(Long _id){
        borrowerRepository.deleteById(_id);
    }


    public List<Borrower> saveBorrowers(List<Borrower> borrowers){
        borrowerRepository.saveAll(borrowers);

        return borrowers;
    }


    public void removeAll(){
        borrowerRepository.deleteAll();
    }



}
