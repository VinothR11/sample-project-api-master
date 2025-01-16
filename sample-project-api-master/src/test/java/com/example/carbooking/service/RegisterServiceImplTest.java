package com.example.carbooking.service;

import com.example.carbooking.dto.LoginDto;
import com.example.carbooking.entities.RegisterEntity;
import com.example.carbooking.exception.ConflictException;
import com.example.carbooking.repository.RegisterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterServiceImplTest {

    @Mock
    private RegisterRepository registerRepository;

    @InjectMocks
    private RegisterServiceImpl registerService;

    private RegisterEntity registerEntity;
    private LoginDto loginDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        registerEntity = new RegisterEntity();
        registerEntity.setUsername("user1");
        registerEntity.setPassword("password123");

        loginDto = new LoginDto();
        loginDto.setUsername("user1");
        loginDto.setPassword("password123");
    }

    @Test
    void testCreateUser_Success() {
        // Arrange
        when(registerRepository.findByUsername(registerEntity.getUsername())).thenReturn(Optional.empty());
        when(registerRepository.save(registerEntity)).thenReturn(registerEntity);

        // Act
        RegisterEntity result = registerService.create(registerEntity);

        // Assert
        assertNotNull(result);
        assertEquals("user1", result.getUsername());
        verify(registerRepository, times(1)).findByUsername(registerEntity.getUsername());
        verify(registerRepository, times(1)).save(registerEntity);
    }

    @Test
    void testCreateUser_UserAlreadyExists() {
        // Arrange
        when(registerRepository.findByUsername(registerEntity.getUsername())).thenReturn(Optional.of(registerEntity));

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> registerService.create(registerEntity));
        assertEquals("User already exists", exception.getMessage());
        verify(registerRepository, times(1)).findByUsername(registerEntity.getUsername());
        verify(registerRepository, times(0)).save(registerEntity);
    }

    @Test
    void testLogin_Success() {
        // Arrange
        when(registerRepository.findByUsername(loginDto.getUsername())).thenReturn(Optional.of(registerEntity));

        // Act
        RegisterEntity result = registerService.login(loginDto);

        // Assert
        assertNotNull(result);
        assertEquals("user1", result.getUsername());
        verify(registerRepository, times(1)).findByUsername(loginDto.getUsername());
    }

    @Test
    void testLogin_UserNotFound() {
        // Arrange
        when(registerRepository.findByUsername(loginDto.getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> registerService.login(loginDto));
        assertEquals("User not found", exception.getMessage());
        verify(registerRepository, times(1)).findByUsername(loginDto.getUsername());
    }

    @Test
    void testLogin_InvalidPassword() {
        // Arrange
        when(registerRepository.findByUsername(loginDto.getUsername())).thenReturn(Optional.of(registerEntity));

        // Act & Assert
        loginDto.setPassword("wrongpassword");
        ConflictException exception = assertThrows(ConflictException.class, () -> registerService.login(loginDto));
        assertEquals("Invalid password", exception.getMessage());
        verify(registerRepository, times(1)).findByUsername(loginDto.getUsername());
    }
}
