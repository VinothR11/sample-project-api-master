package com.example.carbooking.controller;

import com.example.carbooking.controller.UserController;
import com.example.carbooking.entities.UserEntity;
import com.example.carbooking.exception.ConflictException;
import com.example.carbooking.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserServiceImpl userServiceImpl;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        // Prepare mock UserEntity
        userEntity = new UserEntity();
        userEntity.setCustomerid(1);
        userEntity.setName("John Doe");
    }




    // Test case for fetching user by ID (success scenario)
    @Test
    void testGetUserById_Success() throws Exception {
        // Arrange: Mock userService to return a userEntity for ID 1
        when(userServiceImpl.getById(1)).thenReturn(userEntity);

        // Act & Assert: Perform GET request and validate response
        mockMvc.perform(get("/api/user/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerid").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));

        // Verify: Ensure getById method was called once with the ID
        verify(userServiceImpl, times(1)).getById(1);
    }



    // Test case for ending a trip successfully
    @Test
    void testEndTrip_Success() throws Exception {
        // Arrange: Mock userService to return true for successful trip end
        when(userServiceImpl.updateCarAvailability(1L, "2025-01-16 18:00")).thenReturn(true);

        // Act & Assert: Perform PUT request to end trip and validate success message
        mockMvc.perform(put("/api/user/{carid}/endTrip", 1)
                        .param("endTime", "2025-01-16 18:00"))
                .andExpect(status().isOk())
                .andExpect(content().string("Trip ended successfully. Car availability and booking end time updated."));

        // Verify: Ensure updateCarAvailability was called once
        verify(userServiceImpl, times(1)).updateCarAvailability(1L, "2025-01-16 18:00");
    }

    // Test case for ending a trip with failure (Car not found)
    @Test
    void testEndTrip_Failure() throws Exception {
        // Arrange: Mock userService to return false for car update failure
        when(userServiceImpl.updateCarAvailability(1L, "2025-01-16 18:00")).thenReturn(false);

        // Act & Assert: Perform PUT request and expect failure message
        mockMvc.perform(put("/api/user/{carid}/endTrip", 1)
                        .param("endTime", "2025-01-16 18:00"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Car not found or update failed."));

        // Verify: Ensure updateCarAvailability was called once
        verify(userServiceImpl, times(1)).updateCarAvailability(1L, "2025-01-16 18:00");
    }

    // Test case for fetching user by name (success scenario)
    @Test
    void testGetUserByName_Success() throws Exception {
        // Arrange: Mock userService to return userEntity based on name
        when(userServiceImpl.getByName("John Doe")).thenReturn(userEntity);

        // Act & Assert: Perform GET request and validate response
        mockMvc.perform(get("/api/user/name/{name}", "John Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerid").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));

        // Verify: Ensure getByName method was called once with the name
        verify(userServiceImpl, times(1)).getByName("John Doe");
    }
}
