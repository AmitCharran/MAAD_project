package com.revature.maadcars.services;

import com.revature.maadcars.models.Make;
import com.revature.maadcars.models.Model;
import com.revature.maadcars.models.User;
import com.revature.maadcars.models.Vehicle;
import com.revature.maadcars.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:test-application.properties")
class VehicleDeletionPartialIntegratedTest {
    private static final Logger logger = LoggerFactory.getLogger(VehicleDeletionPartialIntegratedTest.class);

    Vehicle vehicle;
    User user;
    Model model;
    Make make;
    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    VehicleService vehicleService;

    @BeforeEach
    void init() {
        model = new Model();
        user = new User();
        make = new Make();

        user.setUser_id(1);
        user.setUsername("testing");
        user.setPassword("testing");
        user.setVehicles(Arrays.asList(vehicle));
        make.setMake_id(1);
        make.setName("test");
        make.setModels(Arrays.asList(model));
        model.setModel_id(1);
        model.setName("test");
        model.setMake(make);
        model.setVehicles(Arrays.asList(vehicle));

        vehicle = new Vehicle();
        vehicle.setVehicle_id(1);
        vehicle.setModel(model);
        vehicle.setUser(user);
        vehicle.setVin("1234567890ABCDEFG");
        vehicle.set_stolen(false);
    }

    @Test
    void deleteVehicle_AlsoDeletesFromUserAndModel() {
        vehicleService.deleteVehicle(1);
        assertTrue(user.getVehicles().isEmpty());
        assertTrue(model.getVehicles().isEmpty());
    }

}
