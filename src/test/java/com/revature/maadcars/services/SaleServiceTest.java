package com.revature.maadcars.services;

import com.revature.maadcars.models.Sale;
import com.revature.maadcars.models.Vehicle;
import com.revature.maadcars.repository.SaleRepository;
import com.revature.maadcars.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import java.sql.Time;


public class SaleServiceTest {
    private SaleRepository saleRepository;
    private SaleService saleService;
    private VehicleRepository vehicleRepository;

    private Sale sale;
    private Vehicle vehicle;
    @BeforeEach
    void setUp(){
        saleRepository = Mockito.mock(SaleRepository.class);
        vehicleRepository = Mockito.mock(VehicleRepository.class);
        saleService = new SaleService(saleRepository, vehicleRepository);

        vehicle = new Vehicle();
        vehicle.setVehicle_id(1);


        sale = new Sale();
        sale.setSale_id(1);
        sale.setVehicle(vehicle);
        sale.setBids(new ArrayList<>());
        sale.setTime_started(new Time(System.currentTimeMillis()));
    }


    /**
     * This test will test that everything works perfectly fine.
     * All info was inputted correctly and get the proper output
     */
    @Test
    void saveSale() {
       when(vehicleRepository.existsById(anyInt())).thenReturn(true);
       when(saleRepository.save(sale)).thenReturn(sale);
       assertEquals(sale, saleService.saveSale(sale));
    }

    /**
     * This test will ensure that IllegalArugmentException is thrown when user tries to pass in a vehicle that does not exists
     */
    @Test
    void saveSaleButVehicleDoesNotExists() {
        when(vehicleRepository.existsById(anyInt())).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> {
            saleService.saveSale(sale);
                });
    }

    /**
     * This test will ensure that everything will work perfectly fine if a user does not pass in a vehicle at all
     */
    @Test
    void saveSaleButVehicleIsNull(){
        sale.setVehicle(null);
        assertThrows(IllegalArgumentException.class, () -> {
            saleService.saveSale(sale);
        });
    }



}
