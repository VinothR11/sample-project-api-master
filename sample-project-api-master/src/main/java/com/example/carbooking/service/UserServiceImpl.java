package com.example.carbooking.service;


import com.example.carbooking.entities.BookingEntity;
import com.example.carbooking.entities.CarEntity;
import com.example.carbooking.entities.UserEntity;
import com.example.carbooking.exception.ConflictException;
import com.example.carbooking.repository.BookingRepository;
import com.example.carbooking.repository.CarRepository;
import com.example.carbooking.repository.UserRepository;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Override
    public UserEntity create(UserEntity userEntity) {
        Optional<UserEntity> customerid = userRepository.findByCustomerid(userEntity.getCustomerid());
        if (customerid.isPresent()) {
            throw new ConflictException("User already exists");
        }
        return userRepository.save(userEntity);
    }
    @Override
    public UserEntity getByName(String name) {
        Optional<UserEntity> findByName = userRepository.findByName(name);
        if (findByName.isPresent()) {
            return findByName.get();
        }
        throw new ConflictException("Add the User details");
    }
    @Override
    public boolean updateCarAvailability(Long carid, String endTime) {
        Optional<CarEntity> carDetails = carRepository.findById(carid);

        if (carDetails.isPresent()) {
            CarEntity carEntity = carDetails.get();


            carEntity.setAvailability(!carEntity.isAvailability());


            List<BookingEntity> bookingEntity = bookingRepository.findByCarid(carid);

            for (BookingEntity booking : bookingEntity) {
                booking.setEndtime(endTime);
                bookingRepository.save(booking);
            }

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
    // method used for calling test method
    public Object getAllUsers() {
        return new ArrayList<>();
    }
}

