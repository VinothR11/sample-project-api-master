package com.example.carbooking.service;

import com.example.carbooking.entities.BookingEntity;
import com.example.carbooking.entities.CarEntity;

import java.util.List;

public interface BookingService {
    BookingEntity booked (BookingEntity bookingEntity);
    List<CarEntity> getActiveCar();
}
