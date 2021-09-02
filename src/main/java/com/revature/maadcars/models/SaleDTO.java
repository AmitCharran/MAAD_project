package com.revature.maadcars.models;

import com.revature.maadcars.services.VehicleService;
import com.revature.maadcars.util.MaadCarsModelMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * SaleDTO: This class will take JSON objects with int values for vehicle_id
 * and is used to access the Vehicle Object for
 */
@Setter
@Getter
@NoArgsConstructor
public class SaleDTO {
    private int sale_id;
    private int vehicle_id;
    private Time time_started;

    /**
     * Returns the vehicle object by giving the vehicle_id
     * @param vehicleService used to access vehicle object
     * @return Vehicle Object or null (if id does not exists)
     */
    public Vehicle getVehicleObject(VehicleService vehicleService) {
        return vehicleService.getVehicleByVehicleId(vehicle_id);
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

        Vehicle vehicle =  saleDTO.getVehicleObject(vehicleService);
        if(vehicle != null) {
            sale.setVehicle(saleDTO.getVehicleObject(vehicleService));
        }else{
            //TODO: log vehicle does not exists
            throw new IllegalArgumentException("Vehicle does not exists. Must create vehicle first");
        }
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
