package com.example.carbooking.service;

import com.example.carbooking.entities.BookingEntity;
import com.example.carbooking.entities.RegisterEntity;
import com.example.carbooking.entities.UserEntity;
import com.example.carbooking.exception.ConflictException;
import com.example.carbooking.repository.BookingRepository;
import com.example.carbooking.repository.CarRepository;
import com.example.carbooking.repository.RegisterRepository;
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

class AdminServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RegisterRepository registerRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers_Success() {
        // Arrange
        UserEntity user1 = new UserEntity();
        UserEntity user2 = new UserEntity();
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<UserEntity> result = adminService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetAllUsers_EmptyList() {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of());

        // Act
        List<UserEntity> result = adminService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetAllDrivers_Success() {
        // Arrange
        RegisterEntity driver1 = new RegisterEntity();
        RegisterEntity driver2 = new RegisterEntity();
        when(registerRepository.findByUsertype("driver")).thenReturn(Arrays.asList(driver1, driver2));

        // Act
        List<RegisterEntity> result = adminService.getAllDrivers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(registerRepository, times(1)).findByUsertype("driver");
    }

    @Test
    void testGetAllDrivers_EmptyList() {
        // Arrange
        when(registerRepository.findByUsertype("driver")).thenReturn(List.of());

        // Act
        List<RegisterEntity> result = adminService.getAllDrivers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(registerRepository, times(1)).findByUsertype("driver");
    }

    @Test
    void testGetAllBookings_Success() {
        // Arrange
        BookingEntity booking1 = new BookingEntity();
        BookingEntity booking2 = new BookingEntity();
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(booking1, booking2));

        // Act
        List<BookingEntity> result = adminService.getAllBooking();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void testGetAllBookings_EmptyList() {
        // Arrange
        when(bookingRepository.findAll()).thenReturn(List.of());

        // Act
        List<BookingEntity> result = adminService.getAllBooking();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).findAll();
    }

    /*@Test
    void testGetUserById_Success() {
        // Arrange
        int userId = 1;
        UserEntity user = new UserEntity();
        user.setId(userId);  // Make sure the ID is correctly set to 1
        when(userRepository.findByCustomerid(userId)).thenReturn(Optional.of(user));

        // Act
        UserEntity result = adminService.getById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());  // This should pass because the mock is correct
        verify(userRepository, times(1)).findByCustomerid(userId);
    }*/

    @Test
    void testGetUserById_NotFound() {
        // Arrange
        int userId = 1;
        when(userRepository.findByCustomerid(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> adminService.getById(userId));
        assertEquals("Add the User details", exception.getMessage());
        verify(userRepository, times(1)).findByCustomerid(userId);
    }
}
