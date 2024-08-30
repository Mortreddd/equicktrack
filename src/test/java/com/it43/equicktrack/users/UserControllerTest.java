package com.it43.equicktrack.users;

import com.it43.equicktrack.transaction.TransactionService;
import com.it43.equicktrack.user.UserController;
import com.it43.equicktrack.user.UserService;
import com.it43.equicktrack.util.AuthenticatedUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = UserControllerTest.class)
public class UserControllerTest {

    @MockBean
    private UserController userController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(userController).isNotNull();
    }
}
