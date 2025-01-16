package com.example.carbooking.repository;

import com.example.carbooking.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity>findByCustomerid(int id);
    Optional<UserEntity>findByEmailaddress(String emailaddress);
    Optional<UserEntity>findByName(String name);
    List<UserEntity> findAll();

}
