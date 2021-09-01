package com.revature.maadcars.models;
import com.revature.maadcars.services.VehicleService;
import com.revature.maadcars.util.MaadCarsModelMapper;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;

/**
 * SaleDTO: This class will take JSON objects with int values for vehicle_id
 * and is used to access the Vehicle Object for
 */
public class SaleDTO {

    private int sale_id;
    private int vehicle_id;
    private Timestamp time_started;


    public SaleDTO(){
    }

    public int getSale_id() {
        return sale_id;
    }

    public void setSale_id(int sale_id) {
        this.sale_id = sale_id;
    }

    public Vehicle getVehicle(VehicleService vehicleService) {
        return vehicleService.getVehicleByVehicleId(vehicle_id);
    }

    public void setVehicle_id(int vehicleId) {
        vehicle_id = vehicleId;

    }

    public Timestamp getTime_started() {
        return time_started;
    }

    public void setTime_started(Timestamp time_started) {
        this.time_started = time_started;
    }

    /**
     * Converts DTO using a ModelMapper to Sale entity.
     * Takes foreign key of vehicle_id and turns it into a vehicle object.
     * @param saleDTO saleDTO object obtained from RequestBody
     * @param vehicleService used to access the vehicle database for Vehicle Object
     * @return Sale object
     */
    public static Sale convertToEntity(SaleDTO saleDTO, VehicleService vehicleService) {
        ModelMapper modelMapper = MaadCarsModelMapper.modelMapper();
        Sale sale = modelMapper.map(saleDTO, Sale.class);
        sale.setVehicle(saleDTO.getVehicle(vehicleService));
        return sale;
    }

    /**
     * Takes in a Sale object and returns a SaleDTO object.
     * The vehicle_id will be changed to int instead of the vehicle object
     * @param sale Sale Object
     * @return SaleDTO Object
     */
    public static SaleDTO convertToDto(Sale sale){
        ModelMapper modelMapper = MaadCarsModelMapper.modelMapper();
        SaleDTO saleDTO = modelMapper.map(sale, SaleDTO.class);
        saleDTO.setVehicle_id(sale.getVehicle().getVehicle_id());
        return saleDTO;
    }
}
