package com.it43.equicktrack.borrower;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BorrowerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowerService borrowerService;

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String token;


    @BeforeEach
    public void setUp() throws Exception{
        Role adminRole = Role.builder()
                .id(1)
                .name("ADMIN")
                .build();
        Role borrowerRole = Role.builder()
                .id(2)
                .name("BORROWER")
                .build();

        roleRepository.saveAll(List.of(adminRole, borrowerRole));
        List<Borrower> borrowers = List.of(
                Borrower.builder()
                        .id(1L)
                        .firstName("Emmanuel")
                        .lastName("Male")
                        .roles(Set.of(adminRole))
                        .email("emmanmale@gmail.com")
                        .password("password")
                        .build(),


                Borrower.builder()
                        .id(2L)
                        .firstName("Bernadette")
                        .lastName("Velasquez")
                        .roles(Set.of(borrowerRole))
                        .email("bernadette@gmail.com")
                        .password("12345678")
                        .build()
        );

        borrowerRepository.saveAll(borrowers);


    }
    @Test
    public void testGetBorrowers() throws Exception {
        Role adminRole = roleRepository.findById(1).orElseThrow();
        Role borrowerRole = roleRepository.findById(2).orElseThrow();


        Mockito.when(borrowerService.getBorrowers()).thenReturn(borrowers);

//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/borrowers")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].id").value(1L))
//                .andExpect(jsonPath("$[0].firstName").value("Emmanuel"))
//                .andExpect(jsonPath("$[0].lastName").value("Male"))
//                .andExpect(jsonPath("$[0].email").value("emmanmale@gmail.com"))
//                .andExpect(jsonPath("$[1].id").value(2L))
//                .andExpect(jsonPath("$[1].firstName").value("Bernadette"))
//                .andExpect(jsonPath("$[1].lastName").value("Velasquez"))
//                .andExpect(jsonPath("$[1].email").value("bernadette@gmail.com"));
    }

    @Test
    public void testGetBorrowerById() throws Exception {
        Role adminRole = Role.builder()
                .id(1)
                .name("ADMIN")
                .build();
        Role borrowerRole = Role.builder()
                .id(2)
                .name("BORROWER")
                .build();

        roleRepository.saveAll(List.of(adminRole, borrowerRole));
        Borrower borrower1 = Borrower.builder()
                .id(1L)
                .firstName("Emmanuel")
                .lastName("Male")
                .roles(Set.of(adminRole))
                .email("emmanmale@gmail.com")
                .password(passwordEncoder.encode("password"))
                .build();

        Borrower borrower2 = Borrower.builder()
                .id(2L)
                .firstName("Bernadette")
                .lastName("Velasquez")
                .email("bernadette@gmail.com")
                .roles(Set.of(borrowerRole))
                .password(passwordEncoder.encode("12345678"))
                .build();

        borrowerRepository.saveAll(List.of(borrower1, borrower2));
        // Mock the service to return the borrower for the given ID
        Mockito.when(borrowerService.getBorrowerById(1L)).thenReturn(Optional.of(borrower1));
        Mockito.when(borrowerService.getBorrowerById(2L)).thenReturn(Optional.of(borrower2));
        Mockito.when(borrowerService.getBorrowerById(3L)).thenReturn(Optional.empty());

        // Test for a borrower that exists
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/borrowers/1")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.firstName").value("Emmanuel"))
//                .andExpect(jsonPath("$.lastName").value("Male"))
//                .andExpect(jsonPath("$.email").value("emmanmale@gmail.com"));
//
//        // Test for a borrower that does not exist
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/borrowers/3")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
    }
}
