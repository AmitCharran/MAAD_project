package com.revature.maadcars.services;

import com.revature.maadcars.models.Vehicle;
import com.revature.maadcars.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

class VehicleServiceTest {

    @MockBean
    Vehicle vehicleMock;
    @MockBean
    VehicleRepository vehicleRepositoryMock;

    VehicleService service;

    @BeforeEach
    void setUp() {

        service = new VehicleService(vehicleRepositoryMock);

        
    }

    @Test
    void saveVehicle() {
        when(vehicleRepositoryMock.save(vehicleMock)).thenReturn(vehicleMock);

        service.saveVehicle(vehicleMock);
    }

    @Test
    void getVehicleByVehicleId() {
    }

    @Test
    void getAllVehicles() {
    }

    @Test
    void deleteVehicle() {
    }
}