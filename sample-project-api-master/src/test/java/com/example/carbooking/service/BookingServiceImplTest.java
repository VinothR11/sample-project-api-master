package com.example.carbooking.service;

import com.example.carbooking.entities.BookingEntity;
import com.example.carbooking.entities.CarEntity;
import com.example.carbooking.exception.ConflictException;
import com.example.carbooking.repository.BookingRepository;
import com.example.carbooking.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBooked_Success() {
        // Arrange
        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setCarid(1L);   // Valid car ID
        bookingEntity.setUserid(1);   // Valid user ID

        when(bookingRepository.save(bookingEntity)).thenReturn(bookingEntity);

        // Act
        BookingEntity result = bookingService.booked(bookingEntity);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getCarid());
        assertEquals(1, result.getUserid());
        verify(bookingRepository, times(1)).save(bookingEntity);
    }

    @Test
    void testBooked_InvalidCarId() {
        // Arrange
        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setCarid(0L);  // Invalid car ID
        bookingEntity.setUserid(1);  // Valid user ID

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> bookingService.booked(bookingEntity));
        assertEquals("Invalid car ID", exception.getMessage());
        verify(bookingRepository, times(0)).save(bookingEntity);
    }

    @Test
    void testBooked_InvalidUserId() {
        // Arrange
        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setCarid(1L);   // Valid car ID
        bookingEntity.setUserid(0);   // Invalid user ID

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> bookingService.booked(bookingEntity));
        assertEquals("Invalid user ID", exception.getMessage());
        verify(bookingRepository, times(0)).save(bookingEntity);
    }

    @Test
    void testGetActiveCar_Success() {
        // Arrange
        CarEntity car1 = new CarEntity();
        car1.setAvailability(true);
        CarEntity car2 = new CarEntity();
        car2.setAvailability(true);

        when(carRepository.findByAvailabilityTrue()).thenReturn(Arrays.asList(car1, car2));

        // Act
        List<CarEntity> result = bookingService.getActiveCar();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(carRepository, times(1)).findByAvailabilityTrue();
    }

    @Test
    void testGetActiveCar_EmptyList() {
        // Arrange
        when(carRepository.findByAvailabilityTrue()).thenReturn(List.of());

        // Act
        List<CarEntity> result = bookingService.getActiveCar();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(carRepository, times(1)).findByAvailabilityTrue();
    }
}
