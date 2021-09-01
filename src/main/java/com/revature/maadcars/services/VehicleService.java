package com.revature.maadcars.services;

import com.revature.maadcars.models.Vehicle;
import com.revature.maadcars.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Service-layer implementation of Vehicle Entity.
 */
@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    /**
     * Injects repository dependency
     */
    @Autowired
    public VehicleService(VehicleRepository vehicleRepository){
        this.vehicleRepository = vehicleRepository;
    }
    /**
     * (Repository method call) Persists input Vehicle into 1 row
     * @param vehicle Vehicle object
     * @return Same Vehicle as input(?)
     */
    public Vehicle saveVehicle(Vehicle vehicle){
        return vehicleRepository.save(vehicle);
    }
    /**
     * (Repository method call) Gets 1 Vehicle by Vehicle ID
     * @param id int
     * @return Vehicle row (only one)
     */
    public Vehicle getVehicleByVehicleId(Integer id){
        return vehicleRepository.findById(id).orElse(null);
    }

    /**
     * (Repository method call) Gets 1 Vehicle by VIN
     * @param vin String
     * @return Vehicle row (only one)
     */
    public Vehicle getVehicleByVin(String vin){
        return vehicleRepository.findByVin(vin).orElse(null);
    }
    /**
     * (Repository method call) Gets List of all Vehicles
     * @return List<Vehicle>
     */
    public List<Vehicle> getAllVehicles(){
        return vehicleRepository.findAll();
    }
    /**
     * (Repository method call) Deletes row that corresponds to input Vehicle if present
     * @param vehicleId int
     */
    public void deleteVehicle(Integer vehicleId){
        vehicleRepository.findById(vehicleId).ifPresent(vehicleRepository::delete);
    }
}