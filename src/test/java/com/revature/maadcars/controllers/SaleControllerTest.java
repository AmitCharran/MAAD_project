package com.revature.maadcars.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.revature.maadcars.models.*;
import com.revature.maadcars.services.BidService;
import com.revature.maadcars.services.SaleService;
import com.revature.maadcars.services.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SaleControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private SaleController saleController;

    @MockBean
    private SaleService saleService;

    @MockBean
    private VehicleService vehicleService;

    @MockBean
    private BidService bidService;



    private Sale sale;
    private Sale sale2;
    private SaleDTO saleDTO;
    private Vehicle vehicle;
    private Vehicle vehicle2;
    private List<Sale> listSale;

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(saleController).build();

        vehicle = new Vehicle();
        vehicle.setVehicle_id(1);
        vehicle.setColor("blue");
        vehicle.setVin("12345678901234567");
        vehicle.set_stolen(false);
        vehicle.setModel(new Model());
        vehicle.setUser(new User());

        vehicle2 = new Vehicle();
        vehicle2.setVehicle_id(2);
        vehicle2.setColor("blue");
        vehicle2.setVin("12345678901234568");
        vehicle2.set_stolen(false);
        vehicle2.setModel(new Model());
        vehicle2.setUser(new User());

        sale = new Sale();
        sale.setSale_id(1);
        sale.setVehicle(vehicle);
        sale.setBids(new ArrayList<>());
        sale.setTime_started(new Time(System.currentTimeMillis()));

        sale2 = new Sale();
        sale2.setSale_id(1);
        sale2.setVehicle(vehicle2);
        sale2.setBids(new ArrayList<>());
        sale2.setTime_started(new Time(System.currentTimeMillis()));

        listSale = new ArrayList<>();
        listSale.add(sale2);

        saleDTO = new SaleDTO();
        saleDTO.setSale_id(1);
        saleDTO.setVehicle_id(1);
        saleDTO.setTime_started(new Time(System.currentTimeMillis()));
    }

    /**
     * This test will make sure the creation of a Sale is working perfectly fine when user inputs values correctly
     */
    @Test
    void createSaleEverythingIsGood() throws Exception {
        when(saleService.saveSale(any(Sale.class))).thenReturn(sale);
        when(saleService.getAllSales()).thenReturn(listSale);
        when(vehicleService.getVehicleByVehicleId(anyInt())).thenReturn(vehicle2);
        when(bidService.getBidByBidId(anyInt())).thenReturn(null);

        ObjectMapper oM = new ObjectMapper();
        oM.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String objectMapperString = new ObjectMapper().writeValueAsString(saleDTO);

        mockMvc.perform(post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapperString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.sale_id").value(1))
                .andExpect(jsonPath("$.vehicle").exists())
                .andExpect(jsonPath("$.time_started").value(sale.getTime_started().toString()));
    }

    /**
     * This test will return a status 400 because it is trying to create a Sale for a vehicle when it is already on sale
     * @throws Exception
     */
    @Test
    void createSaleVehicleAlreadyOnSale() throws Exception{
        listSale.add(sale);
        when(saleService.saveSale(any(Sale.class))).thenReturn(sale);
        when(saleService.getAllSales()).thenReturn(listSale);
        when(vehicleService.getVehicleByVehicleId(anyInt())).thenReturn(vehicle);
        when(bidService.getBidByBidId(anyInt())).thenReturn(null);

        ObjectMapper oM = new ObjectMapper();
        oM.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String objectMapperString = new ObjectMapper().writeValueAsString(saleDTO);

        mockMvc.perform(post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapperString))
                .andExpect(status().isBadRequest())
                .andReturn();
    }







}
