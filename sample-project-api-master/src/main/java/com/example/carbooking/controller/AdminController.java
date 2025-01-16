package com.example.carbooking.controller;

import com.example.carbooking.entities.BookingEntity;
import com.example.carbooking.entities.CarEntity;
import com.example.carbooking.entities.RegisterEntity;
import com.example.carbooking.entities.UserEntity;
import com.example.carbooking.service.AdminServiceImpl;
import com.example.carbooking.service.CarServiceImpl;
import com.example.carbooking.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private CarServiceImpl carService;
    @Autowired
    private AdminServiceImpl adminService;
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @GetMapping("/driver")
    public ResponseEntity<List<RegisterEntity>> getAllDrivers() {
        List<RegisterEntity> users = adminService.getAllDrivers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/create")
    public CarEntity createCar(@RequestBody CarEntity carEntity) {
        return carService.create(carEntity);
    }
    @GetMapping("/booking")
    public ResponseEntity<List<BookingEntity>>getAllBooking(){
        List<BookingEntity>book=adminService.getAllBooking();
        return ResponseEntity.ok(book);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getById(@PathVariable int id) {
        UserEntity userEntity = userService.getById(id);
        return ResponseEntity.ok(userEntity);
    }
    }
