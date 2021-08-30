package com.revature.maadcars.services;

import com.revature.maadcars.models.Vehicle;
import com.revature.maadcars.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

class VehicleServiceTest {

    Vehicle vehicle;
    VehicleRepository vehicleRepositoryMock;

    VehicleService service;

    @BeforeEach
    void setUp() {

        service = Mockito.mock(VehicleService.class);
        vehicle = Mockito.mock(Vehicle.class);
        vehicleRepositoryMock = Mockito.mock(VehicleRepository.class);
        
    }

    @Test
    void saveVehicle() {
        when(vehicleRepositoryMock.save(any(Vehicle.class))).thenReturn(vehicle);

        service.saveVehicle(new Vehicle());
    }

    @Test
    void getVehicleByVehicleId() {
        when(vehicleRepositoryMock.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(vehicle));

        service.saveVehicle(new Vehicle());
    }

    @Test
    void getAllVehicles() {
    }

    @Test
    void deleteVehicle() {
    }
}