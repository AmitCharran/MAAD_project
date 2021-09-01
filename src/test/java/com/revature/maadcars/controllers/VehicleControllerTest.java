package com.revature.maadcars.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.maadcars.models.*;
import com.revature.maadcars.services.ModelService;
import com.revature.maadcars.services.SaleService;
import com.revature.maadcars.services.UserService;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
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
    private ModelService modelService;
    @MockBean
    private UserService userService;
    @MockBean
    private SaleService saleService;

    @MockBean
    private Model modelMock;
    @MockBean
    private Make makeMock;
    @MockBean
    private User userMock;

    private MockMvc mockMvc;
    private Vehicle vehicle;
    private VehicleDTO vDto;
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
        vehicle.setDescription("none");
      
        vehicles = new ArrayList<>();
        vehicles.add(vehicle);

        vDto = new VehicleDTO();
        vDto.setVehicle_id(1);
        vDto.setVin("1234567890ABCDEFG");
        vDto.setModel_id(1);
        vDto.setUser_id(1);
        vDto.set_stolen(false);
        vDto.setColor("white");
        vDto.setDescription("none");

        userMock.setUser_id(1);
    }

    @Test
    void shouldReturnAllVehiclesWhenGetAllVehicles() throws Exception {
        when(vehicleService.getAllVehicles()).thenReturn(vehicles);

        mockMvc.perform(get("/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0].vehicle_id").value("1"))
                .andExpect(jsonPath("$[0].vin").value("1234567890ABCDEFG"))
                .andExpect(jsonPath("$[0].color").value("white"))
                .andExpect(jsonPath("$[0]._stolen").value("false"))
                .andReturn();
    }

    @Test
    void shouldReturnNoResultMessageWhenGetAllVehiclesIsEmpty() throws Exception {
        when(vehicleService.getAllVehicles()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").value("No results found matching your search."))
                .andReturn();
    }

    @Test
    void shouldReturnVehicleWhenFindVehicleById() throws Exception {
        when(vehicleService.getVehicleByVehicleId(anyInt())).thenReturn(vehicle);

        mockMvc.perform(get("/vehicles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.vehicle_id").value("1"))
                .andExpect(jsonPath("$.vin").value("1234567890ABCDEFG"))
                .andExpect(jsonPath("$.color").value("white"))
                .andExpect(jsonPath("$._stolen").value("false"))
                .andReturn();
    }

    @Test
    void shouldReturnNoResultsMessageWhenFindVehicleByIdIsEmpty() throws Exception {
        when(vehicleService.getVehicleByVehicleId(anyInt())).thenReturn(null);

        mockMvc.perform(get("/vehicles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").value("No results found matching your search."))
                .andReturn();
    }

    /**
     * Perform a POST request on "/vehicles", passing a Json containing a valid vehicle to be persisted. Confirm that the values in the response body match the input vehicle.
     * Service layer is mocked to think no vehicle with overlapping VIN is already in database.
     * Expectation: Status is OK, response body contains the same values as input Vehicle's fields.
     */
    @Test
    void post_ReturnCreatedVehicle() throws Exception {
        when(modelService.getModelByModelId(vDto.getModel_id())).thenReturn(modelMock);
        when(vehicleService.getVehicleByVin(vDto.getVin())).thenReturn(null);
        when(userService.getUserByUserId(1)).thenReturn(userMock);
        when(vehicleService.saveVehicle(any(Vehicle.class))).thenReturn(vehicle);

        mockMvc.perform(post("/vehicles")
                .header("user_id","1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(vDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.vehicle_id").value("1"))
                .andExpect(jsonPath("$.vin").value("1234567890ABCDEFG"))
                .andExpect(jsonPath("$.color").value("white"))
                .andExpect(jsonPath("$._stolen").value("false"))
                .andExpect(jsonPath("$.description").value("none"))
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
        when(modelService.getModelByModelId(vDto.getModel_id())).thenReturn(modelMock);
        when(vehicleService.getVehicleByVin(vDto.getVin())).thenReturn(vehicle);
        when(userService.getUserByUserId(1)).thenReturn(userMock);
        when(vehicleService.saveVehicle(any(Vehicle.class))).thenReturn(vehicle);

        mockMvc.perform(post("/vehicles")
                .header("user_id","1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(vDto)))
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
        when(modelService.getModelByModelId(vDto.getModel_id())).thenReturn(modelMock);
        when(vehicleService.getVehicleByVin(vDto.getVin())).thenReturn(null);
        vDto.setVin("0000");

        mockMvc.perform(post("/vehicles")
                .header("user_id","1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(vDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Vehicle Identification Number must be exactly 17 characters long!"))
                .andReturn();
        logger.trace("Test passed: post_VinNot17Chars_ResponseStatus400");
    }

    @Test
    void post_NotLoggedIn_ResponseStatus403() throws Exception {
        when(modelService.getModelByModelId(vDto.getModel_id())).thenReturn(modelMock);
        when(vehicleService.getVehicleByVin(vDto.getVin())).thenReturn(null);
        when(userService.getUserByUserId(1)).thenReturn(userMock);
        when(vehicleService.saveVehicle(any(Vehicle.class))).thenReturn(vehicle);

        mockMvc.perform(post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(vDto)))
                .andExpect(status().isForbidden())
                .andReturn();
        logger.trace("Test passed: post_NotLoggedIn_ResponseStatus403");
    }

    @Test
    void post_InvalidModelId_ResponseStatus400() throws Exception {
        when(modelService.getModelByModelId(vDto.getModel_id())).thenReturn(null);
        when(vehicleService.getVehicleByVin(vDto.getVin())).thenReturn(null);
        when(userService.getUserByUserId(1)).thenReturn(userMock);
        when(vehicleService.saveVehicle(any(Vehicle.class))).thenReturn(vehicle);

        mockMvc.perform(post("/vehicles")
                .header("user_id","1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(vDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").value("Vehicle's model ID does not exist in database!"))
                .andReturn();
        logger.trace("Test passed: post_ReturnCreatedVehicle");
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
                .andExpect(jsonPath("$.vin").value("1234567890ABCDEFG"))
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
    void delete_ValidRequest() throws Exception {
        when(vehicleService.getVehicleByVehicleId(1)).thenReturn(vehicle);

        mockMvc.perform(delete("/vehicles/1")
                .header("user_id", "1"))
                .andExpect(status().isOk());
        logger.trace("Test passed: delete_ValidRequest");
    }

    @Test
    void delete_VehicleIdNotExists_ResponseStatus404() throws Exception {
        when(vehicleService.getVehicleByVehicleId(1)).thenReturn(null);

        mockMvc.perform(delete("/vehicles/1")
                .header("user_id", "1"))
                .andExpect(status().isNotFound());
        logger.trace("Test passed: delete_VehicleIdNotExists_ResponseStatus404");
    }

    @Test
    void delete_NotLoggedIn_ResponseStatus403() throws Exception {
        when(vehicleService.getVehicleByVehicleId(1)).thenReturn(vehicle);

        mockMvc.perform(delete("/vehicles/1"))
                .andExpect(status().isForbidden());
        logger.trace("Test passed: delete_NotLoggedIn_ResponseStatus403");
    }

    @Test
    void delete_WrongUserId_ResponseStatus403() throws Exception {
        when(vehicleService.getVehicleByVehicleId(1)).thenReturn(vehicle);

        mockMvc.perform(delete("/vehicles/1")
                .header("user_id", "2"))
                .andExpect(status().isForbidden());
        logger.trace("Test passed: delete_WrongUserId_ResponseStatus403");
    }
}
