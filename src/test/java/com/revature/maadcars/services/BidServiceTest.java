package com.revature.maadcars.services;

import com.revature.maadcars.models.Bid;
import com.revature.maadcars.models.Sale;
import com.revature.maadcars.models.User;
import com.revature.maadcars.repository.BidRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Time;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BidServiceTest {
    private BidRepository bidRepository;
    private BidService bidService;
    
    private Bid bid;
    
    @BeforeEach
    void setup(){
        bidRepository = Mockito.mock(BidRepository.class);
        bidService = new BidService(bidRepository);
        
        bid = new Bid();
        bid.setBid_id(1);
        bid.setTime(new Time(System.currentTimeMillis()));
        bid.setBid(102.32);
        bid.setSale(new Sale());
        bid.setUser(new User());
    }

    /**
     * Testing if save bid works properly.
     */
    @Test
    void saveBidTest(){
        when(bidRepository.save(any(Bid.class))).thenReturn(bid);
        assertEquals(bid, bidService.saveBid(bid));
    }


}
