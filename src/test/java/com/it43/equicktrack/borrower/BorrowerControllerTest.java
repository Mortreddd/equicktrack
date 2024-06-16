package com.it43.equicktrack.borrower;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BorrowerControllerTest.class)
@AutoConfigureMockMvc
public class BorrowerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowerService borrowerService;


//    @BeforeEach
//    public void setUp() {
//        // Clear the repositories to ensure a clean state
//        roleRepository.deleteAll();
//        borrowerRepository.deleteAll();
//
//        // Create and save roles
//        Role adminRole = Role.builder().name("ADMIN").build();
//        roleRepository.save(adminRole);
//        Role borrowerRole = Role.builder().name("BORROWER").build();
//        roleRepository.save(borrowerRole);
//
//        // Create and save borrowers
//        Borrower borrower1 = Borrower.builder()
//                .firstName("Emmanuel")
//                .lastName("Male")
//                .email("emmanmale@gmail.com")
//                .password(passwordEncoder.encode("emmanuel"))
//            .roles(Set.of(adminRole))
//            .createdAt(Timestamp.from(Instant.now()))
//            .build();
//
//        Borrower borrower2 = Borrower.builder()
//                .firstName("Bea")
//                .lastName("Mangulabnan")
//                .email("beamangulabnan@gmail.com")
//                .password(passwordEncoder.encode("12345678"))
//                .roles(Set.of(borrowerRole))
//                .createdAt(Timestamp.from(Instant.now()))
//                .build();
//
//        borrowerRepository.save(borrower1);
//        borrowerRepository.save(borrower2);
//
//        // Mock the service method to return the borrower
//        Mockito.when(borrowerService.getBorrowerById(1L)).thenReturn(Optional.of(borrower1));
//    }
//
//    @AfterEach
//    public void tearDown() {
//        // Clear the repositories after each test
//        borrowerRepository.deleteAll();
//        roleRepository.deleteAll();
//    }

    @Test
    public void testUserCanReceiveJwtToken() throws Exception {
        // Retrieve the borrower from the mocked service
//        Borrower borrower = borrowerService.getBorrowerById(1L).orElseThrow(() -> new RuntimeException("User not found"));
//        JwtRequest jwtRequest = JwtRequest.builder()
//                .email(borrower.getEmail())
//                .password("emmanuel")  // Use plain text password for testing
//                .build();

        // Convert the JWT request to JSON

        // Perform the login request and expect a 200 OK status
        Borrower borrowerRequest = Borrower.builder()
                .firstName("Emmanuel")
                .lastName("Male")
                .email("emmanmale@gmail.com")
                .password("emmanuel")
                .roles(Set.of(new Role(1, "ADMIN")))
                .build();

        String borrowerRequestJson = toJsonString(borrowerRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(borrowerRequestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    // Helper method to convert an object to JSON string
    public static String toJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
