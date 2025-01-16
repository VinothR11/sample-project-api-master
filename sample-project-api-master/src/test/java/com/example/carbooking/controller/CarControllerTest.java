package com.example.carbooking.controller;

import com.example.carbooking.entities.BookingEntity;
import com.example.carbooking.entities.CarEntity;
import com.example.carbooking.entities.UserEntity;
import com.example.carbooking.exception.ConflictException;
import com.example.carbooking.service.CarServiceImpl;
import com.example.carbooking.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CarControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CarServiceImpl carServiceImpl;

    @Mock
    private UserServiceImpl userServiceImpl;

    @InjectMocks
    private CarController carController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
    }

    @Test
    void getCarById() throws Exception {
        // Arrange
        Long carId = 1L;
        CarEntity carEntity = new CarEntity();
        carEntity.setId(carId);
        when(carServiceImpl.getById(carId)).thenReturn(carEntity);

        // Act & Assert
        mockMvc.perform(get("/api/car/{id}", carId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(carId));

        verify(carServiceImpl, times(1)).getById(carId);
    }

    @Test
    void getBookingCarId() throws Exception {
        // Arrange
        Long carId = 1L;
        BookingEntity bookingEntity = new BookingEntity();
        when(carServiceImpl.getBookingDetails(carId)).thenReturn(List.of(bookingEntity));

        // Act & Assert
        mockMvc.perform(get("/api/car/booking").param("carid", carId.toString()))
                .andExpect(status().isOk());

        verify(carServiceImpl, times(1)).getBookingDetails(carId);
    }


    @Test
    void acceptRide() throws Exception {
        // Arrange
        Long carId = 1L;
        when(carServiceImpl.UpdateCarAvabality(carId)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(put("/api/car/accept").param("carid", carId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Ride has Confirmed"));

        verify(carServiceImpl, times(1)).UpdateCarAvabality(carId);
    }

    @Test
    void acceptRide_NotFound() throws Exception {
        // Arrange
        Long carId = 1L;
        when(carServiceImpl.UpdateCarAvabality(carId)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(put("/api/car/accept").param("carid", carId.toString()))
                .andExpect(status().isNotFound());

        verify(carServiceImpl, times(1)).UpdateCarAvabality(carId);
    }



    @Test
    void getCarsByOwnerById() throws Exception {
        // Arrange
        Long driverId = 1L;
        CarEntity carEntity = new CarEntity();
        when(carServiceImpl.getCarsOwnerByid(driverId)).thenReturn(List.of(carEntity));

        // Act & Assert
        mockMvc.perform(get("/api/car/driver/{driverid}", driverId))
                .andExpect(status().isOk());

        verify(carServiceImpl, times(1)).getCarsOwnerByid(driverId);
    }

    @Test
    void getCarsByOwnerById_NoCars() throws Exception {
        // Arrange
        Long driverId = 1L;
        when(carServiceImpl.getCarsOwnerByid(driverId)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/car/driver/{driverid}", driverId))
                .andExpect(status().isNotFound());

        verify(carServiceImpl, times(1)).getCarsOwnerByid(driverId);
    }
}
