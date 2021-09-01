package com.revature.maadcars.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.maadcars.models.Vehicle;
import com.revature.maadcars.services.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
/**
 * Controller implementation for the Vehicle Entity.
 */
@Controller
@RequestMapping("/vehicles")
public class VehicleController {
    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

    private final VehicleService vehicleService;
    /**
     * Injects service dependency
     */
    @Autowired
    public VehicleController(VehicleService vehicleService){
        this.vehicleService = vehicleService;
    }
    /**
     * Maps "GET Vehicles/" to return a JSON string list of all Vehicles in database.
     * @return ResponseEntity<String>
     */
    @GetMapping
    public @ResponseBody
    ResponseEntity<String> getAllVehicles() throws JsonProcessingException {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        if(vehicles.isEmpty()){
            return ResponseEntity.ok().body("No results found matching your search.");
        }
        return ResponseEntity.ok().body(new ObjectMapper().writeValueAsString(vehicles));
    }
    /**
     * Maps "GET Vehicles/{id}" to return the JSON string Vehicle with that Vehicle_id.
     * @param id = {id} (int)
     * @return ResponseEntity<String>
     */
    @GetMapping("/{id}") // /vehicles/9
    public @ResponseBody
    ResponseEntity<String> findVehicleById(@PathVariable String id) throws JsonProcessingException {
        Vehicle v = vehicleService.getVehicleByVehicleId(Integer.parseInt(id));
        if(v == null){
            return ResponseEntity.ok().body("No results found matching your search.");
        }
        return ResponseEntity.ok().body(new ObjectMapper().writeValueAsString(v));
    }
    /**
     * Maps POST Method to creation of a new persisted Vehicle based on request body.
     * Includes check to throw exceptions if a vehicle with the same VIN is already in the database, or if VIN of vehicle provided is not exactly 17 chars long.
     * @param v Vehicle object interpreted from request body.
     * @return ResponseEntity with status code 200 OK and Json of inserted Vehicle if successful, or a 4xx status code with error message in response body if input fails validation.
     */
    @PostMapping
    public @ResponseBody
    ResponseEntity<String> createVehicle(@RequestBody Vehicle v) throws JsonProcessingException {
        try {
            if (vehicleService.getVehicleByVin(v.getVin()) != null) {
                logger.info("Attempted to insert vehicle with overlapping VIN: " + v.getVin() + " and failed.");
                return ResponseEntity.unprocessableEntity().body("Violation of UNIQUE constraint on 'vin' column in 'vehicles' table!");
            }
            if (v.getVin().length() != 17) {
                logger.info("Attempted to insert vehicle with invalid VIN: " + v.getVin() + " and failed.");
                return ResponseEntity.badRequest().body("Vehicle Identification Number must be exactly 17 characters long!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
            logger.warn(Arrays.toString(e.getStackTrace()));
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
        Vehicle objInserted = vehicleService.saveVehicle(v);
        String strJson = new ObjectMapper().writeValueAsString(objInserted);
        logger.trace("Successfully inserted new Vehicle; assigned ID: " + objInserted.getVehicle_id());
        return ResponseEntity.ok()
                .body(strJson);
    }
    /**
     * Maps PUT Method to updating and persisting the Vehicle that matches the request body.
     * @param v Vehicle object interpreted from request body.
     * @return Updated ResponseEntity<String>.
     */
    @PutMapping
    public @ResponseBody
    ResponseEntity<String> updateVehicle(@RequestBody Vehicle v) throws JsonProcessingException {
        try{
            if(vehicleService.getVehicleByVehicleId(v.getVehicle_id()) == null){
                logger.info("Attempted to update a vehicle that was not present in the database.");
                return ResponseEntity.notFound().build();
            }
        } catch(RuntimeException e){
            logger.warn("Failed to update vehicle.",e);
            return ResponseEntity.badRequest().body("Improper input when updating vehicle.");
        }
        return ResponseEntity.ok().body(new ObjectMapper().writeValueAsString(vehicleService.saveVehicle(v)));
    }
    /**
     * Maps "DELETE Vehicles/{id}" to deletion of a persisted Vehicle by their Vehicle_id. Includes check
     * @param vehicle_id {id} (int) ID of vehicle to be deleted
     * @param user_id (String) Request header containing the ID of currently logged-in user
     * @return HTTP Response Status Code OK if no terminal Exceptions thrown and vehicle_id exists in database.
     */
    @DeleteMapping("/{vehicle_id}")
    public @ResponseBody
    ResponseEntity<HttpStatus> deleteVehicle(@PathVariable String vehicle_id, @RequestHeader(value = "user_id", required = false) String user_id){
        Vehicle vehicle = vehicleService.getVehicleByVehicleId(Integer.parseInt(vehicle_id));
        if (vehicle == null) {
            logger.trace("Attempted to delete a vehicle with an ID that is not in database.");
            return ResponseEntity.notFound().build();
        }
        if (user_id == null || user_id.isEmpty() || !user_id.equals(String.valueOf(vehicle.getUser().getUser_id()))) {
            logger.trace("Attempted to delete a vehicle while user is not logged in as vehicle owner.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        vehicleService.deleteVehicle(Integer.parseInt(vehicle_id));
        logger.trace("Successfully deleted vehicle with id: " + vehicle_id);
        return ResponseEntity.ok().build();
    }
}
