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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity userEntity;
    private CarEntity carEntity;
    private BookingEntity bookingEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntity = new UserEntity();
        userEntity.setCustomerid(1);
        userEntity.setName("John Doe");

        carEntity = new CarEntity();
        carEntity.setId(1L);
        carEntity.setAvailability(true);

        bookingEntity = new BookingEntity();
        bookingEntity.setCarid(1L);
        bookingEntity.setEndtime("2024-12-31T23:59:59");
    }

    @Test
    void testCreateUser_Success() {
        // Arrange
        when(userRepository.findByCustomerid(userEntity.getCustomerid())).thenReturn(Optional.empty());
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        // Act
        UserEntity result = userService.create(userEntity);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getCustomerid());
        verify(userRepository, times(1)).findByCustomerid(userEntity.getCustomerid());
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void testCreateUser_UserAlreadyExists() {
        // Arrange
        when(userRepository.findByCustomerid(userEntity.getCustomerid())).thenReturn(Optional.of(userEntity));

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> userService.create(userEntity));
        assertEquals("User already exists", exception.getMessage());
        verify(userRepository, times(1)).findByCustomerid(userEntity.getCustomerid());
        verify(userRepository, times(0)).save(userEntity);
    }

    @Test
    void testGetUserByName_Success() {
        // Arrange
        when(userRepository.findByName(userEntity.getName())).thenReturn(Optional.of(userEntity));

        // Act
        UserEntity result = userService.getByName(userEntity.getName());

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(userRepository, times(1)).findByName(userEntity.getName());
    }

    @Test
    void testGetUserByName_UserNotFound() {
        // Arrange
        when(userRepository.findByName(userEntity.getName())).thenReturn(Optional.empty());

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> userService.getByName(userEntity.getName()));
        assertEquals("Add the User details", exception.getMessage());
        verify(userRepository, times(1)).findByName(userEntity.getName());
    }

    @Test
    void testUpdateCarAvailability_Success() {
        // Arrange
        when(carRepository.findById(carEntity.getId())).thenReturn(Optional.of(carEntity));
        when(bookingRepository.findByCarid(carEntity.getId())).thenReturn(Arrays.asList(bookingEntity));
        when(carRepository.save(carEntity)).thenReturn(carEntity);
        when(bookingRepository.save(bookingEntity)).thenReturn(bookingEntity);

        // Act
        boolean result = userService.updateCarAvailability(carEntity.getId(), "2025-01-01T00:00:00");

        // Assert
        assertTrue(result);
        assertFalse(carEntity.isAvailability());
        verify(carRepository, times(1)).findById(carEntity.getId());
        verify(bookingRepository, times(1)).save(bookingEntity);
    }

    @Test
    void testUpdateCarAvailability_CarNotFound() {
        // Arrange
        when(carRepository.findById(carEntity.getId())).thenReturn(Optional.empty());

        // Act
        boolean result = userService.updateCarAvailability(carEntity.getId(), "2025-01-01T00:00:00");

        // Assert
        assertFalse(result);
        verify(carRepository, times(1)).findById(carEntity.getId());
        verify(bookingRepository, times(0)).save(any());
    }

    @Test
    void testGetUserById_Success() {
        // Arrange
        when(userRepository.findByCustomerid(userEntity.getCustomerid())).thenReturn(Optional.of(userEntity));

        // Act
        UserEntity result = userService.getById(userEntity.getCustomerid());

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getCustomerid());
        verify(userRepository, times(1)).findByCustomerid(userEntity.getCustomerid());
    }

    @Test
    void testGetUserById_UserNotFound() {
        // Arrange
        when(userRepository.findByCustomerid(userEntity.getCustomerid())).thenReturn(Optional.empty());

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> userService.getById(userEntity.getCustomerid()));
        assertEquals("Add the User details", exception.getMessage());
        verify(userRepository, times(1)).findByCustomerid(userEntity.getCustomerid());
    }
}
