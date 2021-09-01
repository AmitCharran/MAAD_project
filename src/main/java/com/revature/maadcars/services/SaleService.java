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
    @Autowired
    private VehicleRepository vehicleRepository;
    private static final Logger logger = LoggerFactory.getLogger(SaleService.class);

    /**
     * Injects repository dependency
     */
    @Autowired
    public SaleService(SaleRepository saleRepository){
        this.saleRepository = saleRepository;
    }
    /**
     * (Repository method call) Persists input Sale into 1 row
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
        return saleRepository.findById(id).orElseThrow(RuntimeException::new);
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

    private boolean vehicleExists(Sale sale){
        if(vehicleRepository.existsById(sale.getVehicle().getVehicle_id())){
            logger.info("Sale is being created for vehicle: " + sale.getVehicle());
            return true;
        }else{
            logger.warn("Cannot create Sale because vehicle does not exists");
            throw new IllegalArgumentException("Vehicle does not exists");

        }
    }
}
