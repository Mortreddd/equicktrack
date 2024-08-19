package com.it43.equicktrack.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = UserRequestTest.class)
@AutoConfigureMockMvc
public class UserRequestTest {

    @Autowired
    private MockMvc mockMvc;

//    @BeforeEach
//    void setUp() throws Exception {
//        mockMvc.perform(get("/api/v1/users/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isUnauthorized());
////                .andExpect(content().json("{\"id\":1,\"firstName\":\"Emmanuel\",\"lastName\":\"Male\",\"email\":\"emmanmale@gmail.com\"}"));
//
//
//        mockMvc.perform(get("/api/v1/users").contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isUnauthorized());
//    }

    @Test
    @Disabled
    void testCanMakeUserResponse() throws Exception {
        mockMvc.perform(get("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
//                .andExpect(content().json("{\"id\":1,\"firstName\":\"Emmanuel\",\"lastName\":\"Male\",\"email\":\"emmanmale@gmail.com\"}"));


        mockMvc.perform(get("/api/v1/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
