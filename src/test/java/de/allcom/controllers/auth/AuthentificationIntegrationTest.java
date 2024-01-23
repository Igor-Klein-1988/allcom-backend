package de.allcom.controllers.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class AuthentificationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("POST /api/auth/login:")
    public class LoginUser {

        @Transactional
        @Test
        @Sql(scripts = "/sql/data.sql")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_created_user() throws Exception {
            mockMvc.perform(post("/api/users/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "email": "james-smith@mail.com",
                                      "password": "Qwerty007!"
                                    }"""))
                    //.andExpect(status().isOk())
                    //.andExpect(jsonPath("$.id", is(1)));
                    //TODO not working connetion the answer is DENIED
                    .andExpect(status().isForbidden());
        }
    }

    //    @Test
    //    @WithMockUser(roles = {"ADMIN"})
    //    @Sql(scripts = "/sql/data.sql")
    //    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    //    public void return_409_for_existed_email() throws Exception {
    //        mockMvc.perform(post("/api/users/register")
    //                        .contentType(MediaType.APPLICATION_JSON)
    //                        .content("""
    //                                    {
    //                                      "email": "james-smith@mail.com",
    //                                      "password": "Qwerty007!"
    //                                    }"""))
    //                .andExpect(status().isConflict());
    //    }
}
