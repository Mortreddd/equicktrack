package com.it43.equicktrack.borrower;

import com.it43.equicktrack.auth.RegisterAuthenticationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public void createNewBorrower(RegisterAuthenticationRequest registerAuthenticationRequest){
        Borrower borrower = Borrower.builder()
                .firstName(registerAuthenticationRequest.getFirstName())
                .lastName(registerAuthenticationRequest.getLastName())
                .email(registerAuthenticationRequest.getEmail())
                .password(passwordEncoder.encode(registerAuthenticationRequest.getPassword()))
                .build();
        Role role = roleRepository.findRoleByName(registerAuthenticationRequest.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        borrower.setRoles(Set.of(role));

        borrowerRepository.save(borrower);
    }

    public void deleteBorrowerById(Long _id){
        borrowerRepository.deleteById(_id);
    }
}
