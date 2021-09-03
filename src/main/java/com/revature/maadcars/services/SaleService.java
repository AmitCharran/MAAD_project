package com.revature.maadcars.services;

import com.revature.maadcars.models.Sale;
import com.revature.maadcars.repository.SaleRepository;
import com.revature.maadcars.repository.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service-layer implementation of Sale Entity.
 */
@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final VehicleRepository vehicleRepository;
    private static final Logger logger = LoggerFactory.getLogger(SaleService.class);

    /**
     * Injects repository dependency
     */
    @Autowired
    public SaleService(SaleRepository saleRepository, VehicleRepository vehicleRepository){
        this.saleRepository = saleRepository;
        this.vehicleRepository = vehicleRepository;
    }
    /**
     * (Repository method call) Persists input Sale into 1 row
     * Also checks if the Vehicle exists before saving into Sales DB
     * @param sale Sale object
     * @return Same Sale as input(?)
     */
    public Sale saveSale(Sale sale){
        if(vehicleExists(sale)) {
            return saleRepository.save(sale);
        }else{
            return sale;
        }
    }
    /**
     * (Repository method call) Gets 1 Sale by Sale ID
     * @param id int
     * @return Sale row (only one)
     */
    public Sale getSaleBySaleId(Integer id){
        return saleRepository.findById(id).orElse(null);
    }
    /**
     * (Repository method call) Gets List of all Sales
     * @return List<Sale>
     */
    public List<Sale> getAllSales(){
        return saleRepository.findAll();
    }
    /**
     * (Repository method call) Deletes row that corresponds to input Sale if present
     * @param saleId int
     */
    public void deleteSale(Integer saleId){
        saleRepository.findById(saleId).ifPresent(saleRepository::delete);
    }



    /**
     * Check to see if vehicle exists within the DB
     * @param sale uses the vehicle object within sale to identify if it exists
     * @return true if vehicle exists or throws an IllegalArgumentException
     */
    private boolean vehicleExists(Sale sale){
        if(sale.getVehicle() == null || !vehicleRepository.existsById(sale.getVehicle().getVehicle_id())){
            logger.warn("Cannot create Sale because vehicle does not exists");
            throw new IllegalArgumentException("Vehicle does not exists");
        }else{
            logger.info("Sale is being created for vehicle: " + sale.getVehicle());
            return true;

        }
    }

}
