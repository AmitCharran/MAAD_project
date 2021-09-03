package com.revature.maadcars.services;

import com.revature.maadcars.models.Bid;
import com.revature.maadcars.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BidService {
    private final BidRepository bidRepository;

    @Autowired
    public BidService(BidRepository bidRepository){
        this.bidRepository = bidRepository;
    }

    /**
     * Takes a bid object and inserts it into our database
     * @param bid Object of type Bid
     * @return The Bid object that is saved
     */
    public Bid saveBid(Bid bid){
        return bidRepository.save(bid);
    }

    /**
     * Takes an integer and returns a Bid object if
     * @param id primary key of the current object
     * @return return Bid object otherwise RuntimeException
     */
    public Bid getBidByBidId(Integer id){
        return bidRepository.findById(id).orElse(null);
    }

    /**
     * return a list of all objects in the table
     * @return a list of Bids
     */
    public List<Bid> getAllBids(){
        return bidRepository.findAll();
    }

    /**
     * delete a row from the Table bid depending on id
     * @param bidId identifies row from table
     */
    public void deleteBid(Integer bidId){
        bidRepository.findById(bidId).ifPresent(bidRepository::delete);
    }
}
