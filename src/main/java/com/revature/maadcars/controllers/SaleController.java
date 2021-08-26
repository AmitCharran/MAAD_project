package com.revature.maadcars.controllers;

import com.revature.maadcars.models.Sale;
import com.revature.maadcars.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("sales")
public class SaleController {
    private final SaleService saleService;

    @Autowired
    public SaleController(SaleService saleService){
        this.saleService = saleService;
    }

    @GetMapping
    public @ResponseBody
    List<Sale> getAllSales(){
        return saleService.getAllSales();
    }

    @GetMapping("/{id}") // /sales/9
    public @ResponseBody
    Sale findSaleById(@PathVariable String id){
        return saleService.getSaleBySaleId(Integer.parseInt(id));
    }

    @PostMapping
    public @ResponseBody
    Sale createSale(@RequestBody Sale s){
        return saleService.saveSale(s);
    }

    @PutMapping
    public @ResponseBody
    Sale updateSale(@RequestBody Sale s){
        return saleService.saveSale(s);
    }

    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity<HttpStatus> deleteSale(@PathVariable String id){
        saleService.deleteSale(Integer.parseInt(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }
}