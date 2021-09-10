package com.revature.maadcars.services;

import com.revature.maadcars.models.Model;
import com.revature.maadcars.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelService {
    private final ModelRepository modelRepository;

    @Autowired
    public ModelService(ModelRepository modelRepository){
        this.modelRepository = modelRepository;
    }
    /**
     * Takes a model object and inserts it into our database
     * @param model Object of type Model
     * @return The Model object that is saved
     */
    public Model saveModel(Model model){
        return modelRepository.save(model);
    }
    /**
     * Takes an integer and returns a Model object if
     * @param id primary key of the current object
     * @return return Model object otherwise RuntimeException
     */
    public Model getModelByModelId(Integer id){
        return modelRepository.findById(id).orElse(null);
    }
    /**
     * return a list of all objects in the table
     * @return a list of Models
     */
    public List<Model> getAllModels(){
        return modelRepository.findAll();
    }
    /**
     * delete a row from the Table model depending on id
     * @param modelId identifies row from table
     */
    public void deleteModel(Integer modelId){
        modelRepository.findById(modelId).ifPresent(modelRepository::delete);
    }
}
