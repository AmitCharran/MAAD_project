package com.revature.maadcars.controllers;

import com.revature.maadcars.models.Model;
import com.revature.maadcars.services.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RequestMapping("models")
public class ModelController {
    private final ModelService modelService;

    /**
     * One-Argument Constructor
     * Sets the bidService to what is passed in the parameter
     * @param modelService
     */
    @Autowired
    public ModelController(ModelService modelService){
        this.modelService = modelService;
    }
    /**
     * return a list of all objects in the table
     * @return a list of Model
     */
    @GetMapping
    public @ResponseBody
    List<Model> getAllModels(){
        return modelService.getAllModels();
    }
    /**
     * Takes an integer and returns a Model object if
     * @param id primary key of the current object
     * @return return Model object otherwise RuntimeException
     */
    @GetMapping("/{id}") // /models/9
    public @ResponseBody
    Model findModelById(@PathVariable String id){
        return modelService.getModelByModelId(Integer.parseInt(id));
    }
    /**
     * Takes a model object and inserts it into our database
     * @param model Object of type Model
     * @return The Model object that is saved
     */
    @PostMapping
    public @ResponseBody
    Model createModel(@RequestBody Model model){
        return modelService.saveModel(model);
    }
    /**
     * Takes a model object's id and changes the values of it in our database.
     * If the object does not exists, create it.
     * @param model Object of type Model
     * @return The Model object that is saved
     */
    @PutMapping
    public @ResponseBody
    Model updateModel(@RequestBody Model model){
        return modelService.saveModel(model);
    }
    /**
     * delete a row from the Table model depending on id.
     * id is String but then parsed to an Integer.
     * @param id identifies row from table
     * @return ok status
     */
    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity<HttpStatus> deleteModel(@PathVariable String id){
        modelService.deleteModel(Integer.parseInt(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
