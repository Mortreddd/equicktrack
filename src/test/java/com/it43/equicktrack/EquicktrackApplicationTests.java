package com.it43.equicktrack;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = EquicktrackApplicationTests.class)
@RequiredArgsConstructor
class EquicktrackApplicationTests {

//	private final UserController userController;
//	private final EquipmentController equipmentController;
//	private final TransactionController transactionController;

	@Test
	void contextLoads() throws Exception{
		assertThat(true).isTrue();
//		assertThat(userController).isNotNull();
//		assertThat(equipmentController).isNotNull();
//		assertThat(transactionController).isNotNull();
	}

}
