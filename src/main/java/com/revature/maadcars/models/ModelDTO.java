package com.revature.maadcars.models;

import com.revature.maadcars.services.MakeService;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;


/**
 * MakeDTO: This class will take JSON objects with int values for make_id and user_id
 * and is used to access the user Object and make Object for conversion
 */
@Setter
@Getter
@NoArgsConstructor
public class ModelDTO {
    private int model_id;
    private int make_id;
    private String name;
    /**
     * Returns the make object by giving the make_id
     * @param makeService used to access make object
     * @return Make Object or null (if id does not exists)
     */
    public Make getMakeObject(MakeService makeService){
        return makeService.getMakeByMakeId(make_id);
    }

    /**
     * Convert ModelDTO using ModelMapper and conversion functions to Model Object.
     * @param makeService used to access Make Database for Make Object
     * @return Model Object
     */
    public Model convertToEntity(MakeService makeService){
        ModelMapper modelMapper = new ModelMapper();
        Model model = modelMapper.map(this, Model.class);
        Make make = this.getMakeObject(makeService);
        if(make == null){
            throw new IllegalArgumentException("Make does not exist");
        }
        model.setMake(make);
        return model;
    }

}
