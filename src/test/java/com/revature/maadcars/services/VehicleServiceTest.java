package com.revature.maadcars.services;

import com.revature.maadcars.models.User;
import com.revature.maadcars.models.Vehicle;
import com.revature.maadcars.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VehicleServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(VehicleServiceTest.class);

    Vehicle vehicle;
    List<Vehicle> vehicles;
    VehicleRepository vehicleRepositoryMock;
    UserService userServiceMock;

    VehicleService service;

    User user;

    @BeforeAll
    static void beforeAll() {
        logger.trace("Now running VehicleService unit tests...");
    }
  
    @BeforeEach
    void setUp() {
        vehicle = Mockito.mock(Vehicle.class);
        vehicles = new ArrayList<>();
        vehicles.add(vehicle);
        vehicleRepositoryMock = Mockito.mock(VehicleRepository.class);
        userServiceMock = Mockito.mock(UserService.class);
        service = new VehicleService(vehicleRepositoryMock, userServiceMock);

        user = Mockito.mock(User.class);
    }

    @Test
    void saveVehicle() {
        when(vehicleRepositoryMock.save(any(Vehicle.class))).thenReturn(vehicle);

        Vehicle veh = service.saveVehicle(new Vehicle());

        assertEquals(vehicle, veh);
    }

    @Test
    void getVehicleByVehicleId() {
        when(vehicleRepositoryMock.findById(anyInt())).thenReturn(Optional.ofNullable(vehicle));

        Vehicle veh = service.getVehicleByVehicleId(1);

        assertEquals(vehicle, veh);
    }
    /**
     * Calls getVehicleByVin() and passes it a String containing the test Vehicle's VIN, and asserts that the returned Vehicle is equivalent to the test Vehicle.
     *
     * Unit testing successful cases of this method is currently not useful for the same reasons as saveVehicle(), and also because in a successful case this method will return before reaching any of its own business logic.
     * TODO: Implement a real test if business logic is added to getVehicleByVin() later.
     */
    @Test
    public void getVehicleByVin_ReturnsVehicle() {
        when(vehicleRepositoryMock.findByVin("1234567890ABCDEFG")).thenReturn(Optional.of(vehicle));

        Vehicle objReturn = service.getVehicleByVin("1234567890ABCDEFG");

        assertEquals(vehicle, objReturn);
        logger.trace("Test passed: getVehicleByVin_ReturnsVehicle");
    }

    /**
     * Calls getVehicleByVin() and mocks the repository returning an empty Optional, and asserts that this returns null.
     */
    @Test
    public void getVehicleByVin_VinNotOnDatabase_ReturnsNull() {
        when(vehicleRepositoryMock.findByVin("00000000000000000")).thenReturn(Optional.empty());

        Vehicle objReturn = service.getVehicleByVin("00000000000000000");

        assertNull(objReturn);
        logger.trace("Test passed: getVehicleByVin_ReturnsVehicle");
    }

    @Test
    void getAllVehicles() {
        when(vehicleRepositoryMock.findAll()).thenReturn(vehicles);

        List<Vehicle> veh = service.getAllVehicles();

        assertEquals(vehicles, veh);
    }

    @Test
    void deleteVehicle_VerifyRepositoryDeleteCall() {
        when(vehicleRepositoryMock.findById(1)).thenReturn(Optional.of(vehicle));

        service.deleteVehicle(1);

        verify(vehicleRepositoryMock).delete(vehicle);
        logger.trace("Test passed: deleteVehicle_VerifyRepositoryCall");
    }

    @Test
    void deleteVehicle_VehicleIdNotFound_VerifyNoRepositoryDeleteCall() {
        when(vehicleRepositoryMock.findById(1)).thenReturn(Optional.empty());

        service.deleteVehicle(1);

        verify(vehicleRepositoryMock, never()).delete(vehicle);
        logger.trace("Test passed: deleteVehicle_VehicleIdNotFound_VerifyNoRepositoryDeleteCall");
    }

    @Test
    void transferVehicle() throws IllegalAccessException{
        when(vehicleRepositoryMock.findById(anyInt())).thenReturn(Optional.of(vehicle));
        when(vehicle.getUser()).thenReturn(user);
        when(vehicle.getUser().getUser_id()).thenReturn(1);
        when(userServiceMock.getUserByUserId(anyInt())).thenReturn(user);
        when(vehicleRepositoryMock.save(any(Vehicle.class))).thenReturn(vehicle);

        Vehicle updatedVehicle = service.transferVehicle(1, 1, 2);
        assertEquals(vehicle, updatedVehicle);
        logger.trace("Test passed: transferVehicle");
    }

    @Test
    void transferVehicle_ToNonExistentUser_ShouldThrowIllegalAccessException() throws IllegalAccessException{
        when(vehicleRepositoryMock.findById(anyInt())).thenReturn(Optional.of(vehicle));
        when(vehicle.getUser()).thenReturn(user);
        when(vehicle.getUser().getUser_id()).thenReturn(1);
        when(userServiceMock.getUserByUserId(anyInt())).thenReturn(null);

        try{
            Vehicle updatedVehicle = service.transferVehicle(1, 1, 2);
            fail("Expected IllegalAccessException to be thrown.");
        }
        catch (IllegalAccessException e){
            //Do nothing because this is desired result
            logger.trace("Test passed: transferVehicle_ToNonExistentUser_ShouldThrowIllegalAccessException");
        }
    }

    @Test
    void transferVehicle_ThatUserDoesntOwn_ShouldThrowIllegalAccessException() throws IllegalAccessException{
        when(vehicleRepositoryMock.findById(anyInt())).thenReturn(Optional.of(vehicle));
        when(vehicle.getUser()).thenReturn(user);
        when(vehicle.getUser().getUser_id()).thenReturn(3);

        try{
            Vehicle updatedVehicle = service.transferVehicle(1, 1, 2);
            fail("Expected IllegalAccessException to be thrown.");
        }
        catch (IllegalAccessException e) {
            //Do nothing because this is desired result
            logger.trace("Test passed: transferVehicle_ThatUserDoesntOwn_ShouldThrowIllegalAccessException");
        }
    }
}
