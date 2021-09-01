package com.revature.maadcars.services;

import com.revature.maadcars.models.User;
import com.revature.maadcars.models.Vehicle;
import com.revature.maadcars.repository.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Service-layer implementation of Vehicle Entity.
 */
@Service
public class VehicleService {
    private static final Logger logger = LoggerFactory.getLogger(VehicleService.class);

    private final VehicleRepository vehicleRepository;
    private final UserService userService;
    /**
     * Injects repository dependency
     */
    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, UserService userService){
        this.vehicleRepository = vehicleRepository;
        this.userService = userService;
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
        return vehicleRepository.findById(id).orElseThrow(RuntimeException::new);
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

    /**
     * Transfer ownership of a vehicle from one owner to another.
     * @param vehicleId - the ID of the Vehicle to be transferred
     * @param currentUserId - the ID of the current logged-in User
     * @param newOwnerId - the ID of the User who will own the Vehicle after the transfer
     * @throws IllegalAccessException - if the current User does not own the Vehicle
     */
    public Vehicle transferVehicle(Integer vehicleId, Integer currentUserId, Integer newOwnerId) throws IllegalAccessException{
        Vehicle vehicle = getVehicleByVehicleId(vehicleId);
        if(vehicle.getUser().getUser_id() == currentUserId){
            try{
                User newUser = userService.getUserByUserId(newOwnerId);
                vehicle.setUser(newUser);
                Vehicle newVehicle = saveVehicle(vehicle);
                logger.info("VehicleID: " + vehicleId + " has been transferred from " +
                            vehicle.getUser().getUsername() + "(ID:" + currentUserId + ") " +
                            newUser.getUsername() + "(ID:" + newOwnerId + ")");
                return newVehicle;
            }
            catch (RuntimeException e){
                logger.warn("Attempted vehicle transfer to nonexistent user ID " + newOwnerId, e);
                throw new IllegalAccessException("That user does not exist.");
            }
        }
        else{
            logger.warn("Illegal vehicle ownership transfer attempt made by UserID:" + currentUserId + " on VehicleID:" + vehicleId);
            throw new IllegalAccessException("You cannot transfer vehicles you do not own.");
        }
    }
}
