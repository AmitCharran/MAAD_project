package com.revature.maadcars.models;

import com.revature.maadcars.services.ModelService;
import com.revature.maadcars.services.SaleService;
import com.revature.maadcars.services.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * DTO for Vehicle model.
 */
@Getter @Setter
@NoArgsConstructor
public class VehicleDTO {
    private int vehicle_id;

    private int user_id;

    private int model_id;

    private int sale_id;

    private String vin;

    private String color;

    private boolean is_stolen;

    private String description;

    public Vehicle toObject(UserService userService, ModelService modelService, SaleService saleService) {
        ModelMapper modelMapper = new ModelMapper();
        Vehicle v = modelMapper.map(this, Vehicle.class);
        v.setUser(userService.getUserByUserId(user_id));
        if (v.getUser().getVehicles() != null) {
            v.getUser().getVehicles().add(v);
        }
        v.setModel(modelService.getModelByModelId(model_id));
        if (v.getUser() != null && v.getUser().getVehicles() != null) {
            v.getModel().getVehicles().add(v);
        }
        v.setSale(saleService.getSaleBySaleId(sale_id));
        if (v.getSale() != null) {
            v.getSale().setVehicle(v);
        }
        return v;
    }
}