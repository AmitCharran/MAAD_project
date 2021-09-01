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
    @Autowired
    private ModelService modelService;
    @Autowired
    private UserService userService;
    @Autowired
    private SaleService saleService;
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
     * @param vDto Vehicle object interpreted from request body.
     * @return ResponseEntity with status code 200 OK and Json of inserted Vehicle if successful, or a 4xx status code with error message in response body if input fails validation.
     */
    @PostMapping
    public @ResponseBody
    ResponseEntity<String> createVehicle(@RequestBody VehicleDTO vDto, @RequestHeader(value = "user_id",required = false) String current_user_id) throws JsonProcessingException {
        try {
            if (modelService.getModelByModelId(vDto.getModel_id()) == null) {
                logger.info("Attempted to insert with nonexistent model ID: " + vDto.getModel_id() + " and failed.");
                return ResponseEntity.badRequest().body("Vehicle's model ID does not exist in database!");
            }
            if (vehicleService.getVehicleByVin(vDto.getVin()) != null) {
                logger.info("Attempted to insert vehicle with overlapping VIN: " + vDto.getVin() + " and failed.");
                return ResponseEntity.unprocessableEntity().body("Violation of UNIQUE constraint on 'vin' column in 'vehicles' table!");
            }
            if (vDto.getVin().length() != 17) {
                logger.info("Attempted to insert vehicle with invalid VIN: " + vDto.getVin() + " and failed.");
                return ResponseEntity.badRequest().body("Vehicle Identification Number must be exactly 17 characters long!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
            logger.warn(Arrays.toString(e.getStackTrace()));
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
        if (current_user_id == null || current_user_id.isEmpty() || userService.getUserByUserId(Integer.parseInt(current_user_id)) == null) {
            logger.trace("Attempted to insert a vehicle while user is not logged in as an extant user.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        logger.trace("Got past input validation for createVehicle! Json dump of VehicleDTO's current state below:");
        String strJson = new ObjectMapper().writeValueAsString(vDto);
        logger.trace(strJson);
        /*Vehicle objInput = new ObjectMapper().readValue(strJson, Vehicle.class);
        objInput.setUser(userService.getUserByUserId(Integer.parseInt(current_user_id)));
        objInput.setModel(modelService.getModelByModelId(vDto.getModel_id()));*/
        Vehicle objInput = vDto.toObject(userService, modelService, saleService);

        Vehicle objInserted = vehicleService.saveVehicle(objInput);
        String strResponseJson = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(objInserted);
        logger.trace("Successfully inserted new Vehicle; assigned ID: " + objInserted.getVehicle_id());
        return ResponseEntity.ok()
                .body(strResponseJson);
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
