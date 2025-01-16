package com.example.carbooking.controller;
import static org.mockito.Mockito.*;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.carbooking.entities.BookingEntity;
import com.example.carbooking.entities.CarEntity;
import com.example.carbooking.entities.RegisterEntity;
import com.example.carbooking.entities.UserEntity;
import com.example.carbooking.service.AdminServiceImpl;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class AdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CarServiceImpl carService;

    @Mock
    private AdminServiceImpl adminService;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        List<UserEntity> users = Arrays.asList(new UserEntity(), new UserEntity());
        when(adminService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(users.size()));

        verify(adminService, times(1)).getAllUsers();
    }


    @Test
    void shouldReturnAllDrivers() throws Exception {
        List<RegisterEntity> drivers = Arrays.asList(new RegisterEntity(), new RegisterEntity());
        when(adminService.getAllDrivers()).thenReturn(drivers);

        mockMvc.perform(get("/api/admin/driver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(drivers.size()));

        verify(adminService, times(1)).getAllDrivers();
    }

    @Test
    void shouldCreateCar() throws Exception {
        CarEntity carEntity = new CarEntity();
        carEntity.setId(1L);
        carEntity.setModel("Toyota"); // Add other necessary fields

        when(carService.create(any(CarEntity.class))).thenReturn(carEntity);

        mockMvc.perform(post("/api/admin/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"model\": \"Toyota\"}") // Add necessary fields in JSON format
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(carEntity.getId()));

        verify(carService, times(1)).create(any(CarEntity.class));
    }



    @Test
    void shouldReturnAllBookings() throws Exception {
        List<BookingEntity> bookings = Arrays.asList(new BookingEntity(), new BookingEntity());
        when(adminService.getAllBooking()).thenReturn(bookings);

        mockMvc.perform(get("/api/admin/booking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(bookings.size()));

        verify(adminService, times(1)).getAllBooking();
    }


    @Test
    void shouldReturnUserById() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);

        when(userService.getById(1)).thenReturn(userEntity);

        mockMvc.perform(get("/api/admin/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userEntity.getId()));

        verify(userService, times(1)).getById(1);  // Verify with int value
    }


}