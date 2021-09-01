package com.revature.maadcars.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.maadcars.models.Vehicle;
import com.revature.maadcars.models.VehicleDTO;
import com.revature.maadcars.services.ModelService;
import com.revature.maadcars.services.SaleService;
import com.revature.maadcars.services.UserService;
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
    private final UserService userService;
    private final ModelService modelService;
    private final SaleService saleService;
    /**
     * Injects service dependency
     */
    @Autowired
    public VehicleController(VehicleService vehicleService, UserService userService, ModelService modelService, SaleService saleService){
        this.vehicleService = vehicleService;
        this.userService = userService;
        this.modelService = modelService;
        this.saleService = saleService;
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

    /**
     * Maps "PUT vehicles/transfer/{vehicle_id}/to/{new_user_id}"
     * @param vehicle_id Vehicle being updated
     * @param new_user_id the User that will be the new owner
     * @param current_user_id currently logged in User
     * @return Vehicle DTO with the new User ID
     */
    @PutMapping("/transfer/{vehicle_id}/to/{new_user_id}")
    public @ResponseBody
    ResponseEntity<VehicleDTO> transfer(@PathVariable String vehicle_id,
                                        @PathVariable String new_user_id,
                                        @RequestHeader(name = "current_user_id") String current_user_id){
        try {
            Vehicle vehicleWithNewOwner = vehicleService.transferVehicle(Integer.parseInt(vehicle_id), Integer.parseInt(current_user_id), Integer.parseInt(new_user_id));
            VehicleDTO vehicleDTO = VehicleDTO.convertToDto(vehicleWithNewOwner, userService, modelService, saleService);
            return new ResponseEntity<VehicleDTO>(vehicleDTO, HttpStatus.OK);
        } catch (IllegalAccessException e) {
            logger.warn(e.getMessage(), e);
            logger.trace(e.getStackTrace().toString());
            return new ResponseEntity<VehicleDTO>(HttpStatus.BAD_REQUEST);
        }
    }
}
