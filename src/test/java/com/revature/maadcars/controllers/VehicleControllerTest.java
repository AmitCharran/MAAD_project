package com.revature.maadcars.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.maadcars.models.*;
import com.revature.maadcars.services.VehicleService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class VehicleControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(VehicleControllerTest.class);

    private MockMvc mockMvc;
    @Autowired
    private VehicleController vehicleController;
    @MockBean
    private VehicleService vehicleService;

    List<Vehicle> mockVehicles;
    Vehicle mockV;
    @MockBean
    User mockU;
    @MockBean
    Make mockMk;
    @MockBean
    Model mockMd;

    @BeforeAll
    static void beforeAll() {
        logger.trace("Now running VehicleController unit tests...");
    }

    /**
     * On startup: setup MockMvc, create a test vehicle by injecting blank a mock user and model into its foreign keys fields, then finally adding it to a mock list of all vehicles.
     */
    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleController).build();
        mockU = new User();
        mockMd = new Model();

        mockVehicles = new ArrayList<>();
        mockV = new Vehicle();
        mockV.setVehicle_id(1);
        mockV.setUser(mockU);
        mockV.setModel(mockMd);
        mockV.setVin("1234567890ABCDEFG");
        mockV.setColor("white");
        mockV.set_stolen(false);
        mockVehicles.add(mockV);
    }

    /**
     * Perform a POST request on "/vehicles", passing a Json containing a valid vehicle to be persisted. Confirm that the values in the response body match the input vehicle.
     * Service layer is mocked to think no vehicle with overlapping VIN is already in database.
     * Expectation: Status is OK, response body contains the same values as input Vehicle's fields.
     */
    @Test
    void post_ReturnCreatedVehicle() throws Exception {
        when(vehicleService.getVehicleByVin(mockV.getVin())).thenReturn(null);
        when(vehicleService.saveVehicle(any(Vehicle.class))).thenReturn(mockV);

        mockMvc.perform(post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(mockV)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.vehicle_id").value("1"))
                .andExpect(jsonPath("$.vin").value("1234567890ABCDEFG"))
                .andExpect(jsonPath("$.color").value("white"))
                .andExpect(jsonPath("$._stolen").value("false"))
                .andReturn();
        logger.trace("Test passed: post_ReturnCreatedVehicle");
    }

    /**
     * Performs a POST request on "/vehicles", passing a Json containing a vehicle whose VIN is identical to one already in the database.
     * Service layer is mocked to think a vehicle with overlapping VIN is already in database.
     * Expectation: Status is 422, response body contains the proper error message.
     */
    @Test
    void post_VinAlreadyInDatabase_ResponseStatus422() throws Exception {
        when(vehicleService.getVehicleByVin(mockV.getVin())).thenReturn(mockV);

        mockMvc.perform(post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(mockV)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$").value("Violation of UNIQUE constraint on 'vin' column in 'vehicles' table!"))
                .andReturn();
        logger.trace("Test passed: post_VinAlreadyInDatabase_ResponseStatus422");
    }

    /**
     * Performs a POST request on "/vehicles", passing a Json containing a vehicle whose VIN is not exactly 17 characters long.
     * Service layer is mocked to think no vehicle with overlapping VIN is already in database.
     * Expectation: Status is 400, response body contains the proper error message.
     */
    @Test
    void post_VinNot17Chars_ResponseStatus400() throws Exception {
        mockV.setVin("0000");

        when(vehicleService.getVehicleByVin(mockV.getVin())).thenReturn(null);

        mockMvc.perform(post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(mockV)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Vehicle Identification Number must be exactly 17 characters long!"))
                .andReturn();
        logger.trace("Test passed: post_VinNot17Chars_ResponseStatus400");
    }
}
