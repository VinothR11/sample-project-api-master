package com.example.carbooking.service;

import com.example.carbooking.entities.CarEntity;
import com.example.carbooking.entities.UserEntity;

import java.util.List;

public interface CarService {
    CarEntity create(CarEntity carEntity);
    CarEntity getById(Long id);
    UserEntity getById(int id);
    List<CarEntity> getCarsOwnerByid(Long driverid);
}
