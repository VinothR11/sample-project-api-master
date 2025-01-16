package com.example.carbooking.controller;

import com.example.carbooking.dto.LoginDto;
import com.example.carbooking.entities.RegisterEntity;
import com.example.carbooking.service.RegisterServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RegisterControllerTest {

    @Mock
    private RegisterServiceImpl registerServiceImpl;

    @InjectMocks
    private RegisterController registerController;

    @Autowired
    private MockMvc mockMvc;

    RegisterControllerTest() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(registerController).build();
    }

    @Test
    void testCreateRegister() throws Exception {
        RegisterEntity registerEntity = new RegisterEntity();
        registerEntity.setId(1);
        registerEntity.setUsername("testUser");
        registerEntity.setUsertype("USER");

        when(registerServiceImpl.create(any(RegisterEntity.class))).thenReturn(registerEntity);

        mockMvc.perform(post("/api/register/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"usertype\":\"USER\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.usertype").value("USER"));
    }

    @Test
    void testLoginSuccess() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testUser");
        loginDto.setPassword("password");

        RegisterEntity registerEntity = new RegisterEntity();
        registerEntity.setId(1);
        registerEntity.setUsername("testUser");
        registerEntity.setUsertype("USER");

        when(registerServiceImpl.login(any(LoginDto.class))).thenReturn(registerEntity);

        String expectedToken = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwidXNlcm5hbWUiOiJwdWdhbCIsInVzZXJ0eXBlIjoiVVNFUiIsImlhdCI6MTUxNjIzOTAyMn0.4D6lEN-mwsNG_-NP4ZoUxcL_Sh8-w-e-hmf-145H2SASRlqd1vbGUAwI8HJEdDbRQLXApWCjVJvaqUjioxM_BQ";

        mockMvc.perform(post("/api/register/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.userType").value("USER"))
                .andExpect(jsonPath("$.token").value(expectedToken))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testLoginFailure() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("wrongUser");
        loginDto.setPassword("wrongPassword");

        when(registerServiceImpl.login(any(LoginDto.class))).thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/api/register/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"wrongUser\",\"password\":\"wrongPassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }
}
