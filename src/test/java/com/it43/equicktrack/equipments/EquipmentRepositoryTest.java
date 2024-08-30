package com.it43.equicktrack.equipments;

import com.it43.equicktrack.equipment.Equipment;
import com.it43.equicktrack.equipment.EquipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class EquipmentRepositoryTest {

    @Autowired
    private EquipmentRepository equipmentRepository;


    @Test
    @Disabled
    void testCanCreateEquipment() throws Exception {
//        Equipment equipment = Equipment.builder()
//                .name("Projector")
//                .available(true)
//                .
    }
}
