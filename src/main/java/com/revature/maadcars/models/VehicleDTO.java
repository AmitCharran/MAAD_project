package com.revature.maadcars.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO for Vehicle model. 'sales' field temporarily removed since there is currently no foreseeable reason for front end to pass 'sales' in a request.
 */
@Getter @Setter
@NoArgsConstructor
public class VehicleDTO {
    private int vehicle_id;

    private int user_id;

    private int model_id;

    private String vin;

    private String color;

    private boolean is_stolen;

    private String description;
}