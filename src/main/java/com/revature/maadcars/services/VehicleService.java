package com.revature.maadcars.services;

import com.revature.maadcars.models.Vehicle;
import com.revature.maadcars.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository){
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle saveVehicle(Vehicle vehicle){
        return vehicleRepository.save(vehicle);
    }

    public Vehicle getVehicleByVehicleId(Integer id){
        return vehicleRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public List<Vehicle> getAllVehicles(){
        return vehicleRepository.findAll();
    }

    public void deleteVehicle(Integer vehicleId){
        vehicleRepository.findById(vehicleId).ifPresent(vehicleRepository::delete);
    }
}
