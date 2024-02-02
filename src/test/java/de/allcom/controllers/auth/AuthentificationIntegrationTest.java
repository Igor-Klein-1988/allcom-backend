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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
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
    @DisplayName("POST /api/auth/login:")
    public class LoginUser {

        @Test
        @Sql(scripts = "/sql/data.sql")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void login_exists_user() throws Exception {
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "email": "james-smith@mail.com",
                                      "password": "Qwerty007!"
                                    }"""))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)));
        }

        @Test
        @Sql(scripts = "/sql/data.sql")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void login_with_not_exists_user() throws Exception {
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "email": "james@mail.com",
                                      "password": "Qwerty007!"
                                    }"""))
                    .andExpect(status().isNotFound());
        }

        @Test
        @Sql(scripts = "/sql/data.sql")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void login_with_exists_user_but_incorrect_password() throws Exception {
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "email": "james-smith@mail.com",
                                      "password": "Qwerty007!_"
                                    }"""))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("POST /api/auth/register:")
    public class RegisterUser {

        @Transactional
        @Test
        @Sql(scripts = "/sql/data.sql")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void register_new_user() throws Exception {
            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "firstName": "Alex",
                                      "lastName": "Schmidt",
                                      "password": "Qwerty007!",
                                      "email": "alex-schmidt@mail.com",
                                      "phoneNumber": "+491753456755",
                                      "companyName": "Allcom GmbH",
                                      "position": "Purchasing manager",
                                      "taxNumber": "3458795653",
                                      "address": {
                                        "postIndex": "10176",
                                        "city": "Berlin",
                                        "street": "Alexanderplatz",
                                        "houseNumber": "1"
                                      },
                                      "checked": false,
                                      "blocked": false
                                    }"""))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(11)));
        }

        @Transactional
        @Test
        @Sql(scripts = "/sql/data.sql")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void register_user_which_already_exists() throws Exception {
            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "firstName": "James",
                                      "lastName": "Smith",
                                      "password": "Qwerty007!",
                                      "email": "james-smith@mail.com",
                                      "phoneNumber": "+491753456755",
                                      "companyName": "Allcom GmbH",
                                      "position": "Purchasing manager",
                                      "taxNumber": "3458795653",
                                      "address": {
                                        "postIndex": "10176",
                                        "city": "Berlin",
                                        "street": "Alexanderplatz",
                                        "houseNumber": "1"
                                      },
                                      "checked": true,
                                      "blocked": true
                                    }"""))
                    .andExpect(status().isConflict());
        }

        @Transactional
        @Test
        @Sql(scripts = "/sql/data.sql")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void register_new_user_with_not_strong_password() throws Exception {
            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "firstName": "Alex",
                                      "lastName": "Schmidt",
                                      "password": "werty007",
                                      "email": "alex-schmidt@mail.com",
                                      "phoneNumber": "+491753456755",
                                      "companyName": "Allcom GmbH",
                                      "position": "Purchasing manager",
                                      "taxNumber": "3458795653",
                                      "address": {
                                        "postIndex": "10176",
                                        "city": "Berlin",
                                        "street": "Alexanderplatz",
                                        "houseNumber": "1"
                                      },
                                      "checked": false,
                                      "blocked": false
                                    }"""))
                    .andExpect(status().isBadRequest());
        }

        @Transactional
        @Test
        @Sql(scripts = "/sql/data.sql")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void register_new_user_with_wrong_email() throws Exception {
            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "firstName": "Alex",
                                      "lastName": "Schmidt",
                                      "password": "Qwerty007!",
                                      "email": "alex-schmidtATmail.com",
                                      "phoneNumber": "+491753456755",
                                      "companyName": "Allcom GmbH",
                                      "position": "Purchasing manager",
                                      "taxNumber": "3458795653",
                                      "address": {
                                        "postIndex": "10176",
                                        "city": "Berlin",
                                        "street": "Alexanderplatz",
                                        "houseNumber": "1"
                                      },
                                      "checked": false,
                                      "blocked": false
                                    }"""))
                    .andExpect(status().isBadRequest());
        }

        @Transactional
        @Test
        @Sql(scripts = "/sql/data.sql")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void register_new_user_with_not_full_address() throws Exception {
            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "firstName": "Alex",
                                      "lastName": "Schmidt",
                                      "password": "Qwerty007!",
                                      "email": "alex-schmidt@mail.com",
                                      "phoneNumber": "+491753456755",
                                      "companyName": "Allcom GmbH",
                                      "position": "Purchasing manager",
                                      "taxNumber": "3458795653",
                                      "address": {
                                        "postIndex": "10176",
                                        "city": "Berlin",
                                        "street": "",
                                        "houseNumber": "1"
                                      },
                                      "checked": false,
                                      "blocked": false
                                    }"""))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /api/auth/logout:")
    public class LogoutUser {

        @Transactional
        @Test
        @Sql(scripts = "/sql/data.sql")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void logout_user() throws Exception {
            MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "email": "james-smith@mail.com",
                                      "password": "Qwerty007!"
                                    }"""))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andReturn();
            String token = mvcResult.getResponse().getHeader("Authorization");

            mockMvc.perform(post("/api/auth/logout")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

    }

}