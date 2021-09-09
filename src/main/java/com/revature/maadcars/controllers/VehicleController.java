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
@CrossOrigin
@RequestMapping("/vehicles")
public class VehicleController {
    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

    private final VehicleService vehicleService;

    private final UserService userService;
    private final ModelService modelService;
    private final SaleService saleService;

    /**
     * Constructor with dependency injection
     */
    @Autowired
    public VehicleController(VehicleService vehicleService, UserService userService, ModelService modelService, SaleService saleService){
        this.vehicleService = vehicleService;
        this.userService = userService;
        this.modelService = modelService;
        this.saleService = saleService;
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
     * Includes input validation checks on VIN, model ID and whether user is logged in.
     * @param vDto (VehicleDTO) DTO of Vehicle object passed in request body.
     * @param current_user_id (String) Value of "user_id" request header.
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
            if (current_user_id == null || current_user_id.isEmpty() || userService.getUserByUserId(Integer.parseInt(current_user_id)) == null) {
                logger.trace("Attempted to insert a vehicle while user is not logged in as an extant user.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
            logger.warn(Arrays.toString(e.getStackTrace()));
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
        logger.trace("Got past input validation for createVehicle! Json dump of VehicleDTO's current state below:");
        String strJson = new ObjectMapper().writeValueAsString(vDto);
        logger.trace(strJson);
        Vehicle objInput = vDto.convertToEntity(userService, modelService, saleService);
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

    /**
     * Maps "PUT vehicles/transfer/{vehicle_id}/to/{new_user_id}" to update of a Vehicle to a different User
     * @param vehicle_id Vehicle being updated
     * @param new_user_id the User that will be the new owner
     * @param current_user_id currently logged in User
     * @return Vehicle DTO with the new User ID
     */
    @PutMapping("/transfer/{vehicle_id}/to/{new_user_id}")
    public @ResponseBody
    ResponseEntity<Vehicle> transfer(@PathVariable String vehicle_id,
                                        @PathVariable String new_user_id,
                                        @RequestHeader(name = "user_id") String current_user_id){
        try {
            Vehicle vehicleWithNewOwner = vehicleService.transferVehicle(Integer.parseInt(vehicle_id), Integer.parseInt(current_user_id), Integer.parseInt(new_user_id));
            //VehicleDTO vehicleDTO = VehicleDTO.convertToDto(vehicleWithNewOwner);
            return new ResponseEntity<>(vehicleWithNewOwner, HttpStatus.OK);
        } catch (IllegalAccessException e) {
            logger.warn(e.getMessage(), e);
            logger.trace(Arrays.toString(e.getStackTrace()));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
