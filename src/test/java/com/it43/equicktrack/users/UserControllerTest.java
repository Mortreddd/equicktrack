package com.it43.equicktrack.users;

import com.it43.equicktrack.user.UserController;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = UserController.class)

public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(userController).isNotNull();
    }
}
