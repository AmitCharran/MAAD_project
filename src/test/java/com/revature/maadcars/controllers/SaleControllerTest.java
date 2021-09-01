package com.revature.maadcars.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.revature.maadcars.models.*;
import com.revature.maadcars.services.SaleService;
import com.revature.maadcars.services.VehicleService;
import org.apache.tomcat.jni.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;


import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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



    private Sale sale;
    private SaleDTO saleDTO;
    private Vehicle vehicle;

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

        sale = new Sale();
        sale.setSale_id(1);
        sale.setVehicle(vehicle);
        sale.setBids(new ArrayList<>());
        sale.setTime_started(Timestamp.valueOf("2016-04-23 18:25:43"));

        saleDTO = new SaleDTO();
        saleDTO.setSale_id(1);
        saleDTO.setVehicle_id(1);
        saleDTO.setTime_started(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void createSale() throws Exception {
        when(saleService.saveSale(any(Sale.class))).thenReturn(sale);
        ObjectMapper oM = new ObjectMapper();
        oM.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String objectMapperString = new ObjectMapper().writeValueAsString(sale);

      //  objectMapperString = objectMapperString.replace("1461450343000", "\"2016-04-23 18:25:43\"");

        mockMvc.perform(post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(objectMapperString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.vehicle_id").value("1"));
    }






}
