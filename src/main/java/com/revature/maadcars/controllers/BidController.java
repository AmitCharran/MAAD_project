package com.revature.maadcars.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.maadcars.models.Bid;
import com.revature.maadcars.models.BidDTO;
import com.revature.maadcars.services.BidService;
import com.revature.maadcars.services.SaleService;
import com.revature.maadcars.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("bids")
public class BidController {
    private final BidService bidService;
    private final UserService userService;
    private final SaleService saleService;
    /**
     * One-Argument Constructor
     * Sets the bidService to what is passed in the parameter
     * @param bidService
     */
    @Autowired
    public BidController(BidService bidService, UserService userService, SaleService saleService){
        this.bidService = bidService;
        this.userService = userService;
        this.saleService = saleService;
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
     * delete a row from the Table bid depending on id.
     * id is String but then parsed to an Integer.
     * @param id identifies row from table
     * @return ok status
     */
    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity<HttpStatus> deleteBid(@PathVariable String id){
        bidService.deleteBid(Integer.parseInt(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }


}
