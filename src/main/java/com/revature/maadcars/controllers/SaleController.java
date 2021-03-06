package com.revature.maadcars.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.maadcars.models.Sale;
import com.revature.maadcars.models.SaleDTO;
import com.revature.maadcars.services.SaleService;
import com.revature.maadcars.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.List;

/**
 * Controller implementation for the Sale Entity.
 */
@Controller
@CrossOrigin
@RequestMapping("sales")
public class SaleController {
    private final VehicleService vehicleService;
    private final SaleService saleService;
    /**
     * Constructor with dependency injection
     */
    @Autowired
    public SaleController(SaleService saleService, VehicleService vehicleService){
        this.vehicleService = vehicleService;
        this.saleService = saleService;
    }
    /**
     * Maps "GET Sales/" to return a list of all Sales in database.
     * @return List<Sale>
     */
    @GetMapping
    public @ResponseBody
    List<Sale> getAllSales(){
        return saleService.getAllSales();
    }
    /**
     * Maps "GET Sales/{id}" to return the Sale with that Sale_id.
     * @param id = {id} (int)
     * @return Sale
     */
    @GetMapping("/{id}") // /sales/9
    public @ResponseBody
    Sale findSaleById(@PathVariable String id){
        return saleService.getSaleBySaleId(Integer.parseInt(id));
    }
    /**
     * Maps POST Method to creation of a new persisted Sale based on request body.
     * @param saleDTO Sale object interpreted from request body.
     * @return Status OK if Vehicle exists, other Status Bad Request
     */
    @PostMapping
    public @ResponseBody
    ResponseEntity<String> createSale(@RequestBody SaleDTO saleDTO) throws JsonProcessingException {
        try {
            List<Sale> sales = getAllSales();
            for(Sale s: sales){
                if( s.getVehicle() != null && s.getVehicle().getVehicle_id() == saleDTO.getVehicle_id()){
                    throw new IllegalArgumentException("Vehicle already on sale");
                }
            }
            saleDTO.setTime_started(new Time(System.currentTimeMillis()));
            Sale sale = SaleDTO.convertToEntity(saleDTO, vehicleService);
            return ResponseEntity.ok().body(new ObjectMapper().writeValueAsString(saleService.saveSale(sale)));
        }catch (IllegalArgumentException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    /**
     * Maps PUT Method to updating and persisting the Sale that matches the request body.
     * @param s Sale object interpreted from request body.
     * @return Updated Sale.
     */
    @PutMapping
    public @ResponseBody
    Sale updateSale(@RequestBody Sale s){
        return saleService.saveSale(s);
    }
    /**
     * Maps "DELETE Sales/{id}" to deletion of a persisted Sale by their Sale_id.
     * @param id {id} (int)
     * @return HTTP Response Status Code OK if no terminal Exceptions thrown.
     */
    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity<HttpStatus> deleteSale(@PathVariable String id){
        saleService.deleteSale(Integer.parseInt(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }

}