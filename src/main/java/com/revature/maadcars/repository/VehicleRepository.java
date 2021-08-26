package com.revature.maadcars.repository;

import com.revature.maadcars.models.User;
import com.revature.maadcars.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
}
