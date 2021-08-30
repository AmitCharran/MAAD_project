package com.revature.maadcars.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.maadcars.models.Vehicle;
import com.revature.maadcars.models.Vehicle;
import com.revature.maadcars.services.VehicleService;
import com.revature.maadcars.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
/**
 * Controller implementation for the Vehicle Entity.
 */
@Controller
@RequestMapping("/vehicles")
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
     * @param v Vehicle object interpreted from request body.
     * @return Persisted Vehicle.
     */
    @PostMapping
    public @ResponseBody
    Vehicle createVehicle(@RequestBody Vehicle v){
        return vehicleService.saveVehicle(v);
    }
    /**
     * Maps PUT Method to updating and persisting the Vehicle that matches the request body.
     * @param v Vehicle object interpreted from request body.
     * @return Updated Vehicle.
     */
    @PutMapping
    public @ResponseBody
    ResponseEntity<String> updateVehicle(@RequestBody Vehicle v) throws JsonProcessingException {
        try{
            if(vehicleService.getVehicleByVehicleId(v.getVehicle_id()) == null){
                return ResponseEntity.notFound().build();
            }
        } catch(RuntimeException e){

            return ResponseEntity.badRequest().body("Improper input when updating vehicle.");
        }
        Vehicle veh = vehicleService.saveVehicle(v);
        String json = new ObjectMapper().writeValueAsString(veh);
        return ResponseEntity.ok().body(json);
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