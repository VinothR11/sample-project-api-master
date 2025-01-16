package com.example.carbooking.repository;

import com.example.carbooking.entities.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity,Long> {

    List<BookingEntity> findByCarid(Long carid);
    List<BookingEntity>findAll();

}
