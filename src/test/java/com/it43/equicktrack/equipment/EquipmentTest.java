package com.it43.equicktrack.equipment;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EquipmentTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipmentService equipmentService;

    @Test
    public void testGetEquipmentById() throws Exception {
        Equipment equipment = Equipment
                .builder()
                .id(1L)
                .name("Projector")
                .image(null)
                .description("This is projector")
                .qrcode("Hello")
                .build();

        Mockito.when(equipmentService.getEquipmentById(1L)).thenReturn(Optional.of(equipment));

//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/equipments/1")
//                        .accept(MediaType.ALL.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.name").value("Projector"))
//                .andExpect(jsonPath("$.description").value("This is projector"))
//                .andExpect(jsonPath("$.qrcode").value("Hello"));
    }

    @Test
    public void testGetEquipmentByQrcode() throws Exception {
        Equipment equipment = Equipment
                .builder()
                .id(1L)
                .name("Projector")
                .image(null)
                .description("This is projector")
                .qrcode("Hello")
                .build();

        Mockito.when(equipmentService.getEquipmentByQrcode("Hello")).thenReturn(Optional.of(equipment));

//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/equipments/qrcode/Hello")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.name").value("Projector"))
//                .andExpect(jsonPath("$.description").value("This is projector"))
//                .andExpect(jsonPath("$.qrcode").value("Hello"));
    }





}
