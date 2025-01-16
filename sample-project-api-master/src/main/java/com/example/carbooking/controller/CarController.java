package com.example.carbooking.controller;

import com.example.carbooking.entities.BookingEntity;
import com.example.carbooking.entities.CarEntity;
import com.example.carbooking.entities.UserEntity;
import com.example.carbooking.exception.ConflictException;
import com.example.carbooking.service.CarServiceImpl;
import com.example.carbooking.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/car")
public class CarController {
    @Autowired
    private CarServiceImpl carServiceImpl;

    @Autowired
    private UserServiceImpl userServiceImpl;

//    @PostMapping("/create")
//    public CarEntity createCar(@RequestBody CarEntity carEntity) {
//        return carServiceImpl.create(carEntity);
//    }
    @GetMapping("/{id}")
    public CarEntity getCarById(@PathVariable Long id) {
        return (CarEntity) carServiceImpl.getById(id);
    }

    @GetMapping("/booking")
    public ResponseEntity<Object> getBookingCarId(@RequestParam Long carid){
        try{
            List<BookingEntity> booking = carServiceImpl.getBookingDetails(carid);
            if (booking.isEmpty()){
                throw new ConflictException("No booking found");
            }
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @PutMapping("/accept")
    public ResponseEntity<String> acceptRide(@RequestParam Long carid){
        boolean isUpdate = carServiceImpl.UpdateCarAvabality(carid);
        if (isUpdate){
            return ResponseEntity.ok("Ride has Confirmed");
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("")
    public ResponseEntity<UserEntity> getById(@RequestParam int id) {
        UserEntity userEntity = userServiceImpl.getById(id);
        return ResponseEntity.ok(userEntity);
    }
    @GetMapping("/driver/{driverid}")
    public ResponseEntity<List<CarEntity>> getCarsByOwnerById(@PathVariable Long driverid){
        List<CarEntity> cars = carServiceImpl.getCarsOwnerByid(driverid);
        if (cars != null && !cars.isEmpty()) {
            return ResponseEntity.ok(cars);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
        }
    }
}
