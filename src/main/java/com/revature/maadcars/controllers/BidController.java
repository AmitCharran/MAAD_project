package com.revature.maadcars.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.maadcars.models.*;
import com.revature.maadcars.services.BidService;
import com.revature.maadcars.services.SaleService;
import com.revature.maadcars.services.UserService;
import com.revature.maadcars.services.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("bids")
public class BidController {
    private static final Logger logger = LoggerFactory.getLogger(BidController.class);

    private final BidService bidService;
    private final UserService userService;
    private final SaleService saleService;
    private final VehicleService vehicleService;
    /**
     * Constructor with dependency injection
     */
    @Autowired
    public BidController(BidService bidService, UserService userService, SaleService saleService, VehicleService vehicleService){
        this.bidService = bidService;
        this.userService = userService;
        this.saleService = saleService;
        this.vehicleService = vehicleService;
    }

    /**
     * return a list of all objects in the table
     * @return a list of Bids
     */
    @GetMapping
    public @ResponseBody
    List<Bid> getAllBids(){
        return bidService.getAllBids();
    }

    /**
     * Takes an integer and returns a Bid object if
     * @param id primary key of the current object
     * @return return Bid object otherwise RuntimeException
     */
    @GetMapping("/{id}") // /bids/9
    public @ResponseBody
    Bid findBidById(@PathVariable String id){
        return bidService.getBidByBidId(Integer.parseInt(id));
    }

    /**
     * Takes a bid object and inserts it into our database
     * @param bidDTO Object of type Bid
     * @return The Bid object that is saved
     */
    @PostMapping
    public @ResponseBody
    ResponseEntity<String> createBid(@RequestBody BidDTO bidDTO) throws JsonProcessingException{
        try {
            bidDTO.setTime(new Time(System.currentTimeMillis()));
            Bid bid = BidDTO.convertToEntity(bidDTO,userService,saleService);
            return ResponseEntity.ok().body(new ObjectMapper().writeValueAsString(bidService.saveBid(bid)));
        }catch (IllegalArgumentException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    /**
     * Takes a bid object's id and changes the values of it in our database.
     * If the object does not exists, create it.
     * @param bid Object of type Bid
     * @return The Bid object that is saved
     */
    @PutMapping
    public @ResponseBody
    Bid updateBid(@RequestBody Bid bid){
        return bidService.saveBid(bid);
    }

    /**
     * Maps "DELETE bids/{id}" to approving a bid on a vehicle the user owns, which leads to the vehicle being transferred to new owner after cascade deleting its sale and bids. finalizeBid() is a wrapper method, separated from this operation's controller-layer functionality to allow @Transactional to be used.
     * @param id (int) ID of bid being approved.
     * @param user_id (int) ID of currently logged in user (read from request header)
     * @return OK if transaction successfully commits.
     */
    @Transactional(rollbackFor = Throwable.class)
    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity<String> finalizeBid(@PathVariable String id, @RequestHeader(value = "user_id", required = false) String user_id) {
        try {
            return doFinalizeBid(id, user_id);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SQLIntegrityConstraintViolationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
            logger.warn(Arrays.toString(e.getStackTrace()));
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    /**
     * Called by finalizeBid() to perform all the functionality as a Transaction. Should ideally rollback all actions if any exception is thrown.
     @throws NoSuchElementException Thrown when bid ID doesn't exist in database.
     @throws SQLIntegrityConstraintViolationException Thrown when either bid or sale is orphaned (bid points to null sale and/or user, or sale points to null vehicle)
     @throws IllegalAccessException Thrown when client is not logged in as owner of vehicle being sold.
     */
    @Transactional(rollbackFor = Throwable.class)
    public @ResponseBody
    ResponseEntity<String> doFinalizeBid(@PathVariable String id, @RequestHeader(value = "user_id", required = false) String user_id) throws SQLIntegrityConstraintViolationException, IllegalAccessException {
        Bid bid;
        Sale sale;
        Vehicle vehicle;
        User seller, buyer;
        int current_user_id = user_id==null?0:Integer.parseInt(user_id);
        bid = bidService.getBidByBidId(Integer.parseInt(id));
        if (bid == null) {
            logger.info("Attempted to finalize bid with nonexistent bid ID: " + id + " and failed!");
            throw new NoSuchElementException("Bid ID does not exist in database!");
            // Should be 404
        }
        buyer = bid.getUser();
        sale = bid.getSale();
        if (sale == null || buyer == null || sale.getVehicle() == null) {
            logger.error("Orphaned bid with ID: " + id + " was accessed!");
            throw new SQLIntegrityConstraintViolationException();
            // Should be 500
        }
        vehicle = sale.getVehicle();
        seller = vehicle.getUser();
        if (current_user_id == 0 || seller.getUser_id() != current_user_id) {
            logger.info("Attempted to finalize a bid while not logged in as the owner of the vehicle being sold.");
            throw new IllegalAccessException("Attempted to finalize a bid while not logged in as the owner of the vehicle being sold.");
            // Should be 403
        }
        logger.info("Attempt to finalize bid with ID " + id + " passed input validation, now being processed...");
        for (Bid b : sale.getBids()) {
            bidService.deleteBid(b.getBid_id());
        }
        saleService.deleteSale(sale.getSale_id());
        vehicleService.transferVehicle(vehicle.getVehicle_id(), current_user_id, buyer.getUser_id());
        return ResponseEntity.ok().build();
    }
}