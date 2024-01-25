package de.allcom.controllers.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class AuthentificationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
<<<<<<<< HEAD:src/test/java/de/allcom/controllers/auth/AuthentificationIntegrationTest.java
    @DisplayName("POST /api/auth/login:")
    @Profile("test")
    public class LoginUser {
========
    @DisplayName("POST /api/users/register:")
    public class RegisterUser {

        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_created_user() throws Exception {
            mockMvc.perform(post("/api/users/getUserProfile")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "firstName": "alex",
                                      "lastName": "schmidt",
                                      "email": "alex-schmidt@mail.com",
                                      "password": "Qwerty007!"
                                    }"""))
                    .andExpect(status().isForbidden());
                    //.andExpect(jsonPath("$.id", is(1)));
        }
>>>>>>>> origin/maiorov/security:src/test/java/de/allcom/controllers/UsersIntegrationTest.java

        @Transactional
        @Test
        @Sql(scripts = "/sql/data.sql")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
<<<<<<<< HEAD:src/test/java/de/allcom/controllers/auth/AuthentificationIntegrationTest.java
        public void return_created_user() throws Exception {
            mockMvc.perform(post("/api/auth/login")
========
        public void return_409_for_existed_email() throws Exception {
            mockMvc.perform(post("/api/users/getAll")
>>>>>>>> origin/maiorov/security:src/test/java/de/allcom/controllers/UsersIntegrationTest.java
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "email": "james-smith@mail.com",
                                      "password": "Qwerty007!"
                                    }"""))
<<<<<<<< HEAD:src/test/java/de/allcom/controllers/auth/AuthentificationIntegrationTest.java
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)));
========
                    //.andExpect(status().isConflict());
                    .andExpect(status().isForbidden());
>>>>>>>> origin/maiorov/security:src/test/java/de/allcom/controllers/UsersIntegrationTest.java
        }
    }
}
