package com.revature.maadcars.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.maadcars.models.Vehicle;
import com.revature.maadcars.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Controller implementation for the Vehicle Entity.
 */
@Controller
@RequestMapping("vehicles")
public class VehicleController {
    private final VehicleService vehicleService;
    /**
     * Injects service dependency
     */
    @Autowired
    public VehicleController(VehicleService vehicleService){
        this.vehicleService = vehicleService;
    }
    /**
     * Maps "GET Vehicles/" to return a list of all Vehicles in database.
     * @return List<Vehicle>
     */
    @GetMapping
    public @ResponseBody
    List<Vehicle> getAllVehicles(){
        return vehicleService.getAllVehicles();
    }
    /**
     * Maps "GET Vehicles/{id}" to return the Vehicle with that Vehicle_id.
     * @param id = {id} (int)
     * @return Vehicle
     */
    @GetMapping("/{id}") // /vehicles/9
    public @ResponseBody
    Vehicle findVehicleById(@PathVariable String id){
        return vehicleService.getVehicleByVehicleId(Integer.parseInt(id));
    }
    /**
     * Maps POST Method to creation of a new persisted Vehicle based on request body.
     * Includes check to throw exceptions if a vehicle with the same VIN is already in the database, or if VIN of vehicle provided is not exactly 17 chars long.
     * @param v Vehicle object interpreted from request body.
     * @return Persisted Vehicle.
     */
    @PostMapping
    public @ResponseBody
    ResponseEntity<String> createVehicle(@RequestBody Vehicle v) throws JsonProcessingException {
        try {
            if (vehicleService.getVehicleByVin(v.getVin()) != null) {
                return ResponseEntity.unprocessableEntity().body("Violation of UNIQUE constraint on 'vin' column in 'vehicles' table!");
            }
            if (v.getVin().length() != 17) {
                return ResponseEntity.badRequest().body("Vehicle Identification Number must be exactly 17 characters long!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
        Vehicle objInserted = vehicleService.saveVehicle(v);
        String strJson = new ObjectMapper().writeValueAsString(objInserted);
        return ResponseEntity.ok()
                .body(strJson);
    }
    /**
     * Maps PUT Method to updating and persisting the Vehicle that matches the request body.
     * @param v Vehicle object interpreted from request body.
     * @return Updated Vehicle.
     */
    @PutMapping
    public @ResponseBody
    Vehicle updateVehicle(@RequestBody Vehicle v){
        return vehicleService.saveVehicle(v);
    }
    /**
     * Maps "DELETE Vehicles/{id}" to deletion of a persisted Vehicle by their Vehicle_id.
     * @param id {id} (int)
     * @return HTTP Response Status Code OK if no terminal Exceptions thrown.
     */
    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity<HttpStatus> deleteVehicle(@PathVariable String id){
        vehicleService.deleteVehicle(Integer.parseInt(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }
}