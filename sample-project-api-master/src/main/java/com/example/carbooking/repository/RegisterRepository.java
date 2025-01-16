package com.example.carbooking.repository;

import com.example.carbooking.entities.RegisterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegisterRepository extends JpaRepository<RegisterEntity,Long> {
    Optional<RegisterEntity> findByUsername(String username);
    Optional<RegisterEntity> findById(int id);
    List<RegisterEntity> findByUsertype(String usertype);

}
