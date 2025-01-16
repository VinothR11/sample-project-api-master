package com.example.carbooking.service;

import com.example.carbooking.entities.BookingEntity;
import com.example.carbooking.entities.CarEntity;
import com.example.carbooking.entities.UserEntity;
import com.example.carbooking.exception.ConflictException;
import com.example.carbooking.repository.BookingRepository;
import com.example.carbooking.repository.CarRepository;
import com.example.carbooking.repository.UserRepository;
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

class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CarServiceImpl carService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCar_Success() {
        // Arrange
        CarEntity carEntity = new CarEntity();
        carEntity.setRegistrationNumber("ABC123");

        when(carRepository.findByRegistrationNumber("ABC123")).thenReturn(Optional.empty());
        when(carRepository.save(carEntity)).thenReturn(carEntity);

        // Act
        CarEntity result = carService.create(carEntity);

        // Assert
        assertNotNull(result);
        assertEquals("ABC123", result.getRegistrationNumber());
        verify(carRepository, times(1)).findByRegistrationNumber("ABC123");
        verify(carRepository, times(1)).save(carEntity);
    }

    @Test
    void testCreateCar_Conflict() {
        // Arrange
        CarEntity carEntity = new CarEntity();
        carEntity.setRegistrationNumber("ABC123");

        when(carRepository.findByRegistrationNumber("ABC123")).thenReturn(Optional.of(carEntity));

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> carService.create(carEntity));
        assertEquals("Car with registration number ABC123 already exists.", exception.getMessage());
        verify(carRepository, times(1)).findByRegistrationNumber("ABC123");
        verify(carRepository, times(0)).save(carEntity);
    }

    @Test
    void testGetCarById_Success() {
        // Arrange
        CarEntity carEntity = new CarEntity();
        carEntity.setId(1L);
        when(carRepository.findById(1L)).thenReturn(Optional.of(carEntity));

        // Act
        CarEntity result = carService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(carRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCarById_NotFound() {
        // Arrange
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> carService.getById(1L));
        assertEquals("Car with ID 1 not found.", exception.getMessage());
        verify(carRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBookingDetails_Success() {
        // Arrange
        BookingEntity booking1 = new BookingEntity();
        BookingEntity booking2 = new BookingEntity();
        Long carId = 1L;
        when(bookingRepository.findByCarid(carId)).thenReturn(Arrays.asList(booking1, booking2));

        // Act
        List<BookingEntity> result = carService.getBookingDetails(carId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookingRepository, times(1)).findByCarid(carId);
    }

    @Test
    void testGetBookingDetails_NoBookings() {
        // Arrange
        Long carId = 1L;
        when(bookingRepository.findByCarid(carId)).thenReturn(Arrays.asList());

        // Act
        List<BookingEntity> result = carService.getBookingDetails(carId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).findByCarid(carId);
    }

    @Test
    void testUpdateCarAvailability_Success() {
        // Arrange
        CarEntity carEntity = new CarEntity();
        carEntity.setId(1L);
        carEntity.setAvailability(true);

        when(carRepository.findById(1L)).thenReturn(Optional.of(carEntity));
        when(carRepository.save(carEntity)).thenReturn(carEntity);

        // Act
        boolean result = carService.UpdateCarAvabality(1L);

        // Assert
        assertTrue(result);
        assertFalse(carEntity.isAvailability());
        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).save(carEntity);
    }

    @Test
    void testUpdateCarAvailability_CarNotFound() {
        // Arrange
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        boolean result = carService.UpdateCarAvabality(1L);

        // Assert
        assertFalse(result);
        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(0)).save(any());
    }

    @Test
    void testGetUserById_Success() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setCustomerid(1);
        when(userRepository.findByCustomerid(1)).thenReturn(Optional.of(userEntity));

        // Act
        UserEntity result = carService.getById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getCustomerid());
        verify(userRepository, times(1)).findByCustomerid(1);
    }

    @Test
    void testGetUserById_NotFound() {
        // Arrange
        when(userRepository.findByCustomerid(1)).thenReturn(Optional.empty());

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> carService.getById(1));
        assertEquals("Add the User details", exception.getMessage());
        verify(userRepository, times(1)).findByCustomerid(1);
    }

    @Test
    void testGetCarsOwnerById_Success() {
        // Arrange
        CarEntity car1 = new CarEntity();
        car1.setDriverid(1L);
        CarEntity car2 = new CarEntity();
        car2.setDriverid(1L);

        when(carRepository.findByDriverid(1L)).thenReturn(Arrays.asList(car1, car2));

        // Act
        List<CarEntity> result = carService.getCarsOwnerByid(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(carRepository, times(1)).findByDriverid(1L);
    }
}
