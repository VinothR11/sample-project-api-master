package com.example.carbooking.controller;
import static org.mockito.Mockito.*;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.carbooking.entities.BookingEntity;
import com.example.carbooking.entities.CarEntity;
import com.example.carbooking.exception.ConflictException;
import com.example.carbooking.service.BookingServiceImpl;
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

class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingServiceImpl bookingServiceImpl;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    void shouldCreateBookingSuccessfully() throws Exception {
        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setId(1);

        when(bookingServiceImpl.booked(any(BookingEntity.class))).thenReturn(bookingEntity);

        mockMvc.perform(post("/api/user/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"details\":\"Test Booking\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(bookingEntity.getId()));

        verify(bookingServiceImpl, times(1)).booked(any(BookingEntity.class));
    }

    @Test
    void shouldHandleConflictExceptionWhileCreatingBooking() throws Exception {
        when(bookingServiceImpl.booked(any(BookingEntity.class))).thenThrow(new ConflictException("Booking conflict occurred"));

        mockMvc.perform(post("/api/user/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"details\":\"Test Booking\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Booking conflict occurred"));

        verify(bookingServiceImpl, times(1)).booked(any(BookingEntity.class));
    }

    @Test
    void shouldHandleGenericExceptionWhileCreatingBooking() throws Exception {
        when(bookingServiceImpl.booked(any(BookingEntity.class))).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/api/user/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"details\":\"Test Booking\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred: Unexpected error"));

        verify(bookingServiceImpl, times(1)).booked(any(BookingEntity.class));
    }

    @Test
    void shouldReturnAvailableDrivers() throws Exception {
        List<CarEntity> availableDrivers = Arrays.asList(new CarEntity(), new CarEntity());
        when(bookingServiceImpl.getActiveCar()).thenReturn(availableDrivers);

        mockMvc.perform(get("/api/user/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(availableDrivers.size()));

        verify(bookingServiceImpl, times(1)).getActiveCar();
    }

    @Test
    void shouldReturnNoContentWhenNoDriversAvailable() throws Exception {
        when(bookingServiceImpl.getActiveCar()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/user/available"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(bookingServiceImpl, times(1)).getActiveCar();
    }

}
