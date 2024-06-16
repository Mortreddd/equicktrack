package com.it43.equicktrack.borrower;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BorrowerDataJpaTest {

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void setUp() {
        Role adminRole = Role.builder().name("ADMIN").build();
        Role borrowerRole = Role.builder().name("BORROWER").build();
        if(!roleRepository.exists(Example.of(adminRole))){
            roleRepository.save(adminRole);
        }
        if (!roleRepository.exists(Example.of(borrowerRole))){
            roleRepository.save(borrowerRole);
        }
        Borrower emmanuelBorrower = Borrower.builder()
                .firstName("Emmanuel")
                .lastName("Male")
                .email("emmanmale@gmail.com")
                .password(passwordEncoder.encode("emmanuel"))
                .roles(Set.of(adminRole))
                .build();
        Borrower beaBorrower = Borrower.builder()
                .firstName("Bea")
                .lastName("Mangulabnan")
                .email("beamangulabnan@gmail.com")
                .password(passwordEncoder.encode("12345678"))
                .roles(Set.of(borrowerRole))
                .build();


        if(!borrowerRepository.exists(Example.of(emmanuelBorrower))){
            borrowerRepository.save(emmanuelBorrower);
        }
        if(!borrowerRepository.exists(Example.of(beaBorrower))){
            borrowerRepository.save(beaBorrower);
        }
    }
    @Test
    public void testGetAllBorrowers() throws Exception{
        List<Borrower> borrowers = borrowerRepository.findAll();
        assertThat(borrowers).isNotEmpty();
        assertThat(borrowers.size()).isEqualTo(2);
    }

    @Test
    public void testGetBorrowerById() throws Exception{
        Borrower borrower1 = borrowerRepository.findByEmail("emmanmale@gmail.com")
                .orElseThrow(() -> new RuntimeException("User not found"));

        assertThat(borrower1.getFirstName()).isEqualTo("Emmanuel");
        assertThat(borrower1.getLastName()).isEqualTo("Male");
        assertThat(borrower1.getEmail()).isEqualTo("emmanmale@gmail.com");

        Borrower borrower2 = borrowerRepository.findByEmail("beamangulabnan@gmail.com")
                .orElseThrow(() -> new RuntimeException("User not found"));

        assertThat(borrower2.getFirstName()).isEqualTo("Bea");
        assertThat(borrower2.getLastName()).isEqualTo("Mangulabnan");
        assertThat(borrower2.getEmail()).isEqualTo("beamangulabnan@gmail.com");
    }

}
