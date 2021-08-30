package com.revature.maadcars.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.maadcars.models.Make;
import com.revature.maadcars.models.Model;
import com.revature.maadcars.models.User;
import com.revature.maadcars.models.Vehicle;
import com.revature.maadcars.services.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VehicleControllerTest {

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

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleController).build();
        userMock = new User();
        modelMock = new Model();

        vehicle = new Vehicle();
        vehicle.setVehicle_id(1);
        vehicle.setVin("1234567890asdfghj");
        vehicle.setModel(modelMock);
        vehicle.setUser(userMock);
        vehicle.set_stolen(false);
        vehicle.setColor("Black");
    }

    @Test
    void getAllVehicles() {
    }

    @Test
    void findVehicleById() {
    }

    @Test
    void createVehicle() {
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