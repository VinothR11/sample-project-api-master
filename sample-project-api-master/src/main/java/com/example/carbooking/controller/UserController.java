package com.example.carbooking.controller;
import com.example.carbooking.entities.UserEntity;
import com.example.carbooking.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @PostMapping("/create")
    public ResponseEntity<UserEntity>createUser( @RequestBody UserEntity userEntity) {
        UserEntity savedEntity= userServiceImpl.create(userEntity);
        return ResponseEntity.ok(savedEntity);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getById(@PathVariable int id) {
        UserEntity userEntity = userServiceImpl.getById(id);
        return ResponseEntity.ok(userEntity);
    }

    @PutMapping("/{carid}/endTrip")
    public ResponseEntity<String> endTrip(@PathVariable Long carid, @RequestParam("endTime") String endTime) {

        boolean isUpdated = userServiceImpl.updateCarAvailability(carid, endTime);

        if (isUpdated) {
            return ResponseEntity.ok("Trip ended successfully. Car availability and booking end time updated.");
        } else {
            return ResponseEntity.status(404).body("Car not found or update failed.");
        }
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<UserEntity> getByName(@PathVariable String name) {
        UserEntity userEntity = userServiceImpl.getByName(name);
        return ResponseEntity.ok(userEntity);
    }

}