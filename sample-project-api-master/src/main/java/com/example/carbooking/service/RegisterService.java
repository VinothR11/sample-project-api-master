package com.example.carbooking.service;

import com.example.carbooking.dto.LoginDto;
import com.example.carbooking.entities.RegisterEntity;

public interface RegisterService {
    RegisterEntity create(RegisterEntity registerEntity);
    RegisterEntity login(LoginDto loginDto);
}
