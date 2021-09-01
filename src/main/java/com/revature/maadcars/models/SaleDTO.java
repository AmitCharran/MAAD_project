package com.revature.maadcars.models;
import com.revature.maadcars.repository.BidRepository;
import com.revature.maadcars.services.BidService;
import com.revature.maadcars.services.VehicleService;
import com.revature.maadcars.util.MaadCarsModelMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    private int bid_id;
    private Timestamp time_started;

    

    /**
     * Returns the vehicle object by giving the vehicle_id
     * @param vehicleService used to access vehicle object
     * @return Vehicle Object or null (if id does not exists)
     */
    public Vehicle getVehicleObject(VehicleService vehicleService) {
        return vehicleService.getVehicleByVehicleId(vehicle_id);
    }

    /**
     * get bid by bidID
     * @param bidService used to access bid objects
     * @return bid Object
     */
    public Bid getBidObject(BidService bidService){
        return bidService.getBidByBidId(bid_id);
    }
    

    /**
     * Converts DTO using a ModelMapper to Sale entity.
     * Takes foreign key of vehicle_id and turns it into a vehicle object.
     * @param saleDTO saleDTO object obtained from RequestBody
     * @param vehicleService used to access the vehicle database for Vehicle Object
     * @return Sale object
     */
    public static Sale convertToEntity(SaleDTO saleDTO, VehicleService vehicleService, BidService bidService) {
        ModelMapper modelMapper = MaadCarsModelMapper.modelMapper();
        Sale sale = modelMapper.map(saleDTO, Sale.class);

        Vehicle vehicle =  saleDTO.getVehicleObject(vehicleService);
        if(vehicle != null) {
            sale.setVehicle(saleDTO.getVehicleObject(vehicleService));
        }else{
            //TODO: log vehicle does not exists
            throw new IllegalArgumentException("Vehicle does not exists. Must create vehicle first");
        }



        Bid bid = bidService.getBidByBidId(saleDTO.getBid_id());
        if(bid == null){
            // TODO: log bid does not exists
            throw new IllegalArgumentException("bid does not exists");
        }
        if(sale.getBids() != null) {
            sale.getBids().add(bidService.getBidByBidId(saleDTO.getBid_id()));
        }else{
            sale.setBids(new ArrayList<>());
            sale.getBids().add(bidService.getBidByBidId(saleDTO.getBid_id()));
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
