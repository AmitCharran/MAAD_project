package com.revature.maadcars.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.revature.maadcars.models.*;
import com.revature.maadcars.services.BidService;
import com.revature.maadcars.services.SaleService;
import com.revature.maadcars.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Time;
import java.util.ArrayList;

@SpringBootTest
@AutoConfigureMockMvc
public class BidControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private BidController bidController;
    @MockBean
    private BidService bidService;
    @MockBean
    private UserService userService;
    @MockBean
    private SaleService saleService;

    private Bid bid;
    private BidDTO bidDTO;
    private User user;
    private Sale sale;



    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(bidController).build();

        user = new User();
        user.setUser_id(1);
        user.setUsername("aaaaaa");
        user.setFirstName("a");
        user.setLastName("b");
        user.setEmail("a@a.com");
        user.setPassword("111111");

        sale = new Sale();
        sale.setBids(new ArrayList<>());
        sale.setVehicle(new Vehicle());

        bid = new Bid();
        bid.setBid_id(1);
        bid.setSale(sale);
        bid.setTime(new Time(System.currentTimeMillis()));
        bid.setUser(user);
        bid.setBid(100.09);

        bidDTO = new BidDTO();
        bidDTO.setSale(1);
        bidDTO.setTime(new Time(System.currentTimeMillis()));
        bidDTO.setUser(1);
        bidDTO.setBid(100.09);
    }

    /**
     * Checks to see if Post works correctly on creating a bid
     */
    @Test
    void createBidOnVehicle() throws Exception {
        when(userService.getUserByUserId(anyInt())).thenReturn(user);
        when(saleService.getSaleBySaleId(anyInt())).thenReturn(sale);
        when(bidService.saveBid(any(Bid.class))).thenReturn(bid);

        ObjectMapper oM = new ObjectMapper();
        oM.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String objectMapperString = new ObjectMapper().writeValueAsString(bidDTO);

        mockMvc.perform(post("/bids")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapperString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.sale").exists())
                .andExpect(jsonPath("$.user").exists())
                .andExpect(jsonPath("$.bid").value(100.09))
                .andExpect(jsonPath("$.time").value(bid.getTime().toString()))
                .andReturn();
    }

    /**
     * checks to see if the post on bid returns bad request status when there are no records in sale
     */
    @Test
    void createBidOnVehicleButItsNotOnSale() throws Exception {
        sale = null;
        when(userService.getUserByUserId(anyInt())).thenReturn(user);
        when(saleService.getSaleBySaleId(anyInt())).thenReturn(sale);
        when(bidService.saveBid(any(Bid.class))).thenReturn(bid);

        mockMvc.perform(post("/bids")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(bidDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    /**
     * checks to see if status is bad request when user does not exists
     */
    @Test
    void createBidOnVehicleButUserDoesNotExists() throws Exception {
        user = null;
        when(userService.getUserByUserId(anyInt())).thenReturn(user);
        when(saleService.getSaleBySaleId(anyInt())).thenReturn(sale);
        when(bidService.saveBid(any(Bid.class))).thenReturn(bid);


        mockMvc.perform(post("/bids")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(bidDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

}
