package com.revature.maadcars.controllers;

import com.revature.maadcars.models.Sale;
import com.revature.maadcars.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller implementation for the Sale Entity.
 */
@Controller
@RequestMapping("sales")
public class SaleController {
    private final SaleService saleService;
    /**
     * Injects service dependency
     */
    @Autowired
    public SaleController(SaleService saleService){
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
     * @param s Sale object interpreted from request body.
     * @return Persisted Sale.
     */
    @PostMapping
    public @ResponseBody
    Sale createSale(@RequestBody Sale s){
        return saleService.saveSale(s);
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