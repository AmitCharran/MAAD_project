package com.revature.maadcars.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.revature.maadcars.models.*;
import com.revature.maadcars.services.BidService;
import com.revature.maadcars.services.SaleService;
import com.revature.maadcars.services.UserService;
import com.revature.maadcars.services.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @MockBean
    private VehicleService vehicleService;

    private Bid bid;
    private BidDTO bidDTO;
    private User user;
    private Sale sale;
    private Vehicle vehicle;

    private User buyer;

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

        vehicle = new Vehicle();
        vehicle.setVehicle_id(1);
        vehicle.setUser(user);

        sale = new Sale();
        sale.setSale_id(1);
        sale.setBids(new ArrayList<>());
        sale.setVehicle(vehicle);
        vehicle.setSale(sale);

        bid = new Bid();
        bid.setBid_id(1);
        bid.setSale(sale);
        bid.setTime(new Time(System.currentTimeMillis()));
        bid.setUser(user);
        bid.setBid(100.09);
        sale.getBids().add(bid);

        bidDTO = new BidDTO();
        bidDTO.setSale_id(1);
        bidDTO.setTime(new Time(System.currentTimeMillis()));
        bidDTO.setUser_id(1);
        bidDTO.setBid(100.09);

        buyer = new User();
        buyer.setUser_id(2);
        buyer.setVehicles(new ArrayList<>());
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

    @Test
    void finalizeBid_ValidRequest() throws Exception {
        bid.setUser(buyer);
        when(bidService.getBidByBidId(1)).thenReturn(bid);
        doNothing().when(bidService).deleteBid(1);
        doNothing().when(saleService).deleteSale(1);
        when(vehicleService.transferVehicle(1,1,2)).thenReturn(vehicle);

        mockMvc.perform(delete("/bids/1")
                .header("user_id",1))
                .andExpect(status().isOk());

        verify(bidService).deleteBid(1);
        verify(saleService).deleteSale(1);
        verify(vehicleService).transferVehicle(1,1,2);
    }

    @Test
    void finalizeBid_BidIdNotExists_StatusCode404() throws Exception {
        bid.setUser(buyer);
        when(bidService.getBidByBidId(1)).thenReturn(null);
        doNothing().when(bidService).deleteBid(1);
        doNothing().when(saleService).deleteSale(1);
        when(vehicleService.transferVehicle(1,1,2)).thenReturn(vehicle);

        mockMvc.perform(delete("/bids/1")
                .header("user_id",1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Bid ID does not exist in database!"));

        verify(bidService).getBidByBidId(1);
        verifyNoMoreInteractions(bidService);
        verifyNoInteractions(saleService);
        verifyNoInteractions(vehicleService);
    }

    @Test
    void finalizeBid_OrphanedBid_StatusCode500() throws Exception {
        bid.setUser(buyer);
        bid.setSale(null);
        when(bidService.getBidByBidId(1)).thenReturn(bid);
        doNothing().when(bidService).deleteBid(1);
        doNothing().when(saleService).deleteSale(1);
        when(vehicleService.transferVehicle(1,1,2)).thenReturn(vehicle);

        mockMvc.perform(delete("/bids/1")
                .header("user_id",1))
                .andExpect(status().isInternalServerError());

        verify(bidService).getBidByBidId(1);
        verifyNoMoreInteractions(bidService);
        verifyNoInteractions(saleService);
        verifyNoInteractions(vehicleService);
    }

    @Test
    void finalizeBid_NotLoggedInAsOwner_StatusCode403() throws Exception {
        bid.setUser(buyer);
        when(bidService.getBidByBidId(1)).thenReturn(bid);
        doNothing().when(bidService).deleteBid(1);
        doNothing().when(saleService).deleteSale(1);
        when(vehicleService.transferVehicle(1,1,2)).thenReturn(vehicle);

        mockMvc.perform(delete("/bids/1")
                .header("user_id",2))
                .andExpect(status().isForbidden());

        verify(bidService).getBidByBidId(1);
        verifyNoMoreInteractions(bidService);
        verifyNoInteractions(saleService);
        verifyNoInteractions(vehicleService);
    }
}