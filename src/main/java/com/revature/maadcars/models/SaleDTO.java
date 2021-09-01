package com.revature.maadcars.models;
import com.revature.maadcars.services.BidService;
import com.revature.maadcars.services.VehicleService;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class SaleDTO {

    private int sale_id;
    private int vehicle_id;
    private int bid_id;
    private Time time_started;


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

    //TODO change to getAllBids
    public List<Bid> getBids(BidService bidService) {
        List<Bid> listBid = new ArrayList<>();
        listBid.add(bidService.getBidByBidId(bid_id));
        return listBid;



    }

    public void setBid_id(int bidId) {
        bid_id = bidId;
    }

    public Time getTime_started() {
        return time_started;
    }

    public void setTime_started(Time time_started) {
        this.time_started = time_started;
    }

}
