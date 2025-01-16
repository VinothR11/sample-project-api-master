package com.example.carbooking.service;

import com.example.carbooking.entities.BookingEntity;
import com.example.carbooking.entities.CarEntity;
import com.example.carbooking.entities.UserEntity;
import com.example.carbooking.exception.ConflictException;
import com.example.carbooking.repository.BookingRepository;
import com.example.carbooking.repository.CarRepository;
import com.example.carbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService{

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public CarEntity create(CarEntity carEntity) {
        Optional<CarEntity> existingCar = carRepository.findByRegistrationNumber(carEntity.getRegistrationNumber());
        if (existingCar.isPresent()) {
            throw new ConflictException("Car with registration number " + carEntity.getRegistrationNumber() + " already exists.");
        }
        return carRepository.save(carEntity);
    }
    @Override
    public CarEntity getById(Long id) {
        Optional<CarEntity> foundCar = carRepository.findById(id);
        if (foundCar.isPresent()) {
            return foundCar.get();
        }
        throw new ConflictException("Car with ID " + id + " not found.");
    }

    public List<BookingEntity> getBookingDetails(Long carid){
        return bookingRepository.findByCarid(carid);

    }
    public boolean UpdateCarAvabality(Long carid){
        Optional<CarEntity> carDetails = carRepository.findById(carid);
        if (carDetails.isPresent()){
            CarEntity carEntity = carDetails.get();
            carEntity.setAvailability(!carEntity.isAvailability());
            carRepository.save(carEntity);
            return true;
        }
        return false;
    }
    @Override
    public UserEntity getById(int id) {
        Optional<UserEntity> findById = userRepository.findByCustomerid(id);
        if (findById.isPresent()) {
            return findById.get();
        }
        throw new ConflictException("Add the User details");
    }
    @Override
    public List<CarEntity> getCarsOwnerByid(Long driverid){
        return carRepository.findByDriverid(driverid);
    }

}
