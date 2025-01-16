package com.example.carbooking.dto;

import com.example.carbooking.dto.LoginDto;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginDtoTest{

    @Test
    void testGettersAndSetterSuccess() {

        LoginDto loginDto = new LoginDto();

        loginDto.setId(1);
        loginDto.setUsername("vinoth");
        loginDto.setPassword("vinoth123");
        loginDto.setUsertype("user");

        assertEquals(1, loginDto.getId());
        assertEquals("vinoth", loginDto.getUsername());
        assertEquals("vinoth123", loginDto.getPassword());
        assertEquals("user", loginDto.getUsertype());
    }
}
