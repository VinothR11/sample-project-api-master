package com.example.carbooking.service;

import com.example.carbooking.entities.BookingEntity;
import com.example.carbooking.entities.CarEntity;
import com.example.carbooking.exception.ConflictException;
import com.example.carbooking.repository.BookingRepository;
import com.example.carbooking.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CarRepository carRepository;

    public BookingEntity booked (BookingEntity bookingEntity){
        if (bookingEntity.getCarid().compareTo(0L) <= 0) {
            throw new ConflictException("Invalid car ID");
        }


        // Use compareTo method to check if userId is greater than 0
        if (Integer.valueOf(bookingEntity.getUserid()).compareTo(0) <= 0) {
            throw new ConflictException("Invalid user ID");
        }

        return bookingRepository.save(bookingEntity);
    }
    public List<CarEntity> getActiveCar(){

        return carRepository.findByAvailabilityTrue();
    }

}

