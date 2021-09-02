package com.revature.maadcars.models;

import com.revature.maadcars.services.ModelService;
import com.revature.maadcars.services.SaleService;
import com.revature.maadcars.services.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

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

    /**
     * Converts this DTO into a Vehicle based on its fields. Accepts a boolean flag as 4th argument to decide whether this method should update the objects the new Vehicle has foreign keys to and update them to also point back at this Vehicle.
     * @param userService Required dependency injection
     * @param modelService Required dependency injection
     * @param saleService Required dependency injection
     * @param updateForeignKeys Optional. Defaults to true if omitted.
     * @return Vehicle with foreign keys properly set.
     */
    public Vehicle convertToEntity(UserService userService, ModelService modelService, SaleService saleService, boolean updateForeignKeys) {
        ModelMapper modelMapper = new ModelMapper();
        Vehicle v = modelMapper.map(this, Vehicle.class);
        v.setUser(userService.getUserByUserId(user_id));
        if (updateForeignKeys && v.getUser() != null && v.getUser().getVehicles() != null) {
            v.getUser().getVehicles().add(v);
        }
        v.setModel(modelService.getModelByModelId(model_id));
        if (updateForeignKeys && v.getUser() != null && v.getUser().getVehicles() != null) {
            v.getModel().getVehicles().add(v);
        }
        v.setSale(saleService.getSaleBySaleId(sale_id));
        if (updateForeignKeys && v.getSale() != null) {
            v.getSale().setVehicle(v);
        }
        return v;
    }

    public Vehicle convertToEntity(UserService userService, ModelService modelService, SaleService saleService) {
        return convertToEntity(userService, modelService, saleService, true);
    }

    /**
     * Static method to convert a Vehicle object to a DTO based on its fields.
     * @param vehicle
     * @param userService Required dependency injection
     * @param modelService Required dependency injection
     * @param saleService Required dependency injection
     * @return VehicleDTO
     */
    public static VehicleDTO convertToDto(Vehicle vehicle) {
        ModelMapper modelMapper = new ModelMapper();
        VehicleDTO vDTO = modelMapper.map(vehicle, VehicleDTO.class);
        vDTO.setUser_id(vehicle.getUser()==null?0:vehicle.getUser().getUser_id());
        vDTO.setModel_id(vehicle.getModel()==null?0:vehicle.getModel().getModel_id());
        vDTO.setSale_id(vehicle.getSale()==null?0:vehicle.getSale().getSale_id());
        return vDTO;
    }
}
