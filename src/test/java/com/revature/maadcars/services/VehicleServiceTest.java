package com.revature.maadcars.services;

import com.revature.maadcars.models.Model;
import com.revature.maadcars.models.User;
import com.revature.maadcars.models.Vehicle;
import com.revature.maadcars.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class VehicleServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(VehicleServiceTest.class);

    @InjectMocks
    VehicleService vehicleService;

    Vehicle mockV;
    @MockBean
    VehicleRepository vehicleRepository;

    @BeforeAll
    static void beforeAll() {
        logger.trace("Now running VehicleService unit tests...");
    }

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        vehicleRepository = mock(VehicleRepository.class);

        vehicleService = new VehicleService(vehicleRepository);

        mockV = new Vehicle();
        mockV.setVehicle_id(1);
        mockV.setUser(new User());
        mockV.setModel(new Model());
        mockV.setVin("1234567890ABCDEFG");
        mockV.setColor("white");
        mockV.set_stolen(false);
    }

    /**
     * Calls saveVehicle() on a test Vehicle object and asserts that it is equal to the Vehicle returned.
     *
     * Unit testing saveVehicle() is not useful currently since all it does is make a single repository-layer call; it does not perform any business logic itself (e.g. contains no service-layer functionality).
     * In practice, saveVehicle() does not actually return an equivalent Vehicle, but rather a Vehicle with field values matching the values persisted onto the SQL database (including a valid unique vehicle_id generated by incrementing from the latest vehicle_id used by existing Vehicles.)
     * TODO: Implement a real test if business logic is added to saveVehicle() later.
     */
    @Test
    public void saveVehicle_ReturnsVehicle() {
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(mockV);

        Vehicle objReturn = vehicleService.saveVehicle(new Vehicle());

        assertEquals(objReturn, mockV);
        logger.trace("Test passed: saveVehicle_ReturnsVehicle");
    }

    /**
     * Calls getVehicleByVin() and passes it a String containing the test Vehicle's VIN, and asserts that the returned Vehicle is equivalent to the test Vehicle.
     *
     * Unit testing successful cases of this method is currently not useful for the same reasons as saveVehicle(), and also because in a successful case this method will return before reaching any of its own business logic.
     * TODO: Implement a real test if business logic is added to getVehicleByVin() later.
     */
    @Test
    public void getVehicleByVin_ReturnsVehicle() {
        when(vehicleRepository.findByVin("1234567890ABCDEFG")).thenReturn(Optional.of(mockV));

        Vehicle objReturn = vehicleService.getVehicleByVin("1234567890ABCDEFG");

        assertEquals(objReturn, mockV);
        logger.trace("Test passed: getVehicleByVin_ReturnsVehicle");
    }

    /**
     * Calls getVehicleByVin() and mocks the repository returning an empty Optional, and asserts that this returns null.
     */
    @Test
    public void getVehicleByVin_VinNotOnDatabase_ReturnsNull() {
        when(vehicleRepository.findByVin("00000000000000000")).thenReturn(Optional.empty());

        Vehicle objReturn = vehicleService.getVehicleByVin("00000000000000000");

        assertNull(objReturn);
        logger.trace("Test passed: getVehicleByVin_ReturnsVehicle");
    }
}
