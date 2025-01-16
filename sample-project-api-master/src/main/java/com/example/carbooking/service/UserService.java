package com.example.carbooking.service;

import com.example.carbooking.entities.UserEntity;

public interface UserService {
    UserEntity create(UserEntity userEntity);
    UserEntity getByName(String name);
    boolean updateCarAvailability(Long carid, String endTime);
    UserEntity getById(int id);

}
