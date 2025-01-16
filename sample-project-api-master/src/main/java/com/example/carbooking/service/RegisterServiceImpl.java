package com.example.carbooking.service;

import com.example.carbooking.dto.LoginDto;
import com.example.carbooking.entities.RegisterEntity;
import com.example.carbooking.exception.ConflictException;
import com.example.carbooking.repository.RegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private RegisterRepository registerRepository;
    @Override
    public RegisterEntity create(RegisterEntity registerEntity) {
        Optional<RegisterEntity> username = registerRepository.findByUsername(registerEntity.getUsername());
        if (username.isPresent()) {
            throw new ConflictException("User already exists");
        }
        return registerRepository.save(registerEntity);
    }
    @Override
    public RegisterEntity login(LoginDto loginDto) {
        Optional<RegisterEntity> registerEntity = registerRepository.findByUsername(loginDto.getUsername());
        if (registerEntity.isEmpty()) {
            throw new ConflictException("User not found");
        }
        if (!registerEntity.get().getPassword().equals(loginDto.getPassword())) {
            throw new ConflictException("Invalid password");
        }
        RegisterEntity users = registerEntity.get();
        return users;
    }

}
