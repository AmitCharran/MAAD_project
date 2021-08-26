package com.revature.maadcars.controllers;

import com.revature.maadcars.models.Vehicle;
import com.revature.maadcars.models.Vehicle;
import com.revature.maadcars.services.VehicleService;
import com.revature.maadcars.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService){
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public @ResponseBody
    List<Vehicle> getAllVehicles(){
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/{id}") // /vehicles/9
    public @ResponseBody
    Vehicle findVehicleById(@PathVariable String id){
        return vehicleService.getVehicleByVehicleId(Integer.parseInt(id));
    }

    @PostMapping
    public @ResponseBody
    Vehicle createVehicle(@RequestBody Vehicle v){
        return vehicleService.saveVehicle(v);
    }

    @PutMapping
    public @ResponseBody
    Vehicle updateVehicle(@RequestBody Vehicle v){
        return vehicleService.saveVehicle(v);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity<HttpStatus> deleteVehicle(@PathVariable String id){
        vehicleService.deleteVehicle(Integer.parseInt(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }


}
