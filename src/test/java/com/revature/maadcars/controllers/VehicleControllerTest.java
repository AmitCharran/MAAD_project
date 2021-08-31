package com.revature.maadcars.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.maadcars.models.Make;
import com.revature.maadcars.models.Model;
import com.revature.maadcars.models.User;
import com.revature.maadcars.models.Vehicle;
import com.revature.maadcars.services.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class VehicleControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(VehicleControllerTest.class);

    @Autowired
    private VehicleController vehicleController;

    @MockBean
    private VehicleService vehicleService;

    @MockBean
    private Model modelMock;
    @MockBean
    private Make makeMock;
    @MockBean
    private User userMock;

    private MockMvc mockMvc;
    private Vehicle vehicle;
    private List<Vehicle> vehicles;

    @BeforeAll
    static void beforeAll() {
        logger.trace("Now running VehicleController unit tests...");
    }
  
    /**
     * On startup: setup MockMvc, create a test vehicle by injecting blank a mock user and model into its foreign keys fields, then finally adding it to a mock list of all vehicles.
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleController).build();
        userMock = new User();
        modelMock = new Model();

        vehicle = new Vehicle();
        vehicle.setVehicle_id(1);
        vehicle.setVin("1234567890ABCDEFG");
        vehicle.setModel(modelMock);
        vehicle.setUser(userMock);
        vehicle.set_stolen(false);
        vehicle.setColor("white");
      
        vehicles = new ArrayList<>();
        vehicles.add(vehicle);
    }

    @Test
    void getAllVehicles() {
    }

    @Test
    void findVehicleById() {
    }

    /**
     * Perform a POST request on "/vehicles", passing a Json containing a valid vehicle to be persisted. Confirm that the values in the response body match the input vehicle.
     * Service layer is mocked to think no vehicle with overlapping VIN is already in database.
     * Expectation: Status is OK, response body contains the same values as input Vehicle's fields.
     */
    @Test
    void post_ReturnCreatedVehicle() throws Exception {
        when(vehicleService.getVehicleByVin(vehicle.getVin())).thenReturn(null);
        when(vehicleService.saveVehicle(any(Vehicle.class))).thenReturn(vehicle);

        mockMvc.perform(post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(vehicle)))
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
        when(vehicleService.getVehicleByVin(vehicle.getVin())).thenReturn(vehicle);

        mockMvc.perform(post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(vehicle)))
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
        vehicle.setVin("0000");

        when(vehicleService.getVehicleByVin(vehicle.getVin())).thenReturn(null);

        mockMvc.perform(post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(vehicle)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Vehicle Identification Number must be exactly 17 characters long!"))
                .andReturn();
        logger.trace("Test passed: post_VinNot17Chars_ResponseStatus400");
    }

    @Test
    void shouldReturnOkVehicleWhenVehicleUpdate() throws Exception {
        when(vehicleService.saveVehicle(any(Vehicle.class))).thenReturn(vehicle);
        when(vehicleService.getVehicleByVehicleId(anyInt())).thenReturn(vehicle);

        mockMvc.perform(put("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(vehicle)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.vehicle_id").value("1"))
                .andExpect(jsonPath("$.vin").value("1234567890asdfghj"))
                .andReturn();
    }

    @Test
    void shouldReturnNotFoundWhenVehicleUpdate() throws Exception {
        when(vehicleService.getVehicleByVehicleId(anyInt())).thenReturn(null);

        mockMvc.perform(put("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(vehicle)))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void deleteVehicle() {
    }
}
