package com.revature.maadcars.models;

import com.revature.maadcars.services.SaleService;
import com.revature.maadcars.services.UserService;
import com.revature.maadcars.services.VehicleService;
import com.revature.maadcars.util.MaadCarsModelMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.sql.Time;

/**
 * SaleDTO: This class will take JSON objects with int values for sale_id and user_id
 * and is used to access the user Object and sale Object for conversion
 */
@Setter
@Getter
@NoArgsConstructor
public class BidDTO {
    private int bid_id;
    private int sale_id;
    private int user_id;
    private Time time;
    private double bid;
    /**
     * Returns the user object by giving the user_id
     * @param userService used to access sale object
     * @return User Object or null (if id does not exists)
     */
    public User getUserObject(UserService userService){
        return userService.getUserByUserId(user_id);
    }
    /**
     * Returns the sale object by giving the sale_id
     * @param saleService used to access sale object
     * @return Sale Object or null (if id does not exists)
     */
    public Sale getSaleObject(SaleService saleService){
        return saleService.getSaleBySaleId(sale_id);
    }

    /**
     * Convert BidDTO using ModelMapper and conversion functions to Bid Object.
     * @param bidDTO object obtained from RequestBody
     * @param userService used to access User Database for User Object
     * @param saleService used to access Sale Database for Sale Object
     * @return Bid Object
     */
    public static Bid convertToEntity(BidDTO bidDTO, UserService userService, SaleService saleService){
        ModelMapper modelMapper = MaadCarsModelMapper.modelMapper();
        Bid bid = modelMapper.map(bidDTO, Bid.class);

        User user = bidDTO.getUserObject(userService);
        if(user == null){
            throw new IllegalArgumentException("User does not exist");
        }
        Sale sale = bidDTO.getSaleObject(saleService);
        if(sale == null){
            throw new IllegalArgumentException("Sale does not exist");
        }

        bid.setUser(user);
        bid.setSale(sale);
        return bid;
    }

}
