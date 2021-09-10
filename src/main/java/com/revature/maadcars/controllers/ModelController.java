package com.revature.maadcars.controllers;

import com.revature.maadcars.models.Model;
import com.revature.maadcars.models.ModelDTO;
import com.revature.maadcars.services.MakeService;
import com.revature.maadcars.services.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@Controller
@CrossOrigin
@RequestMapping("models")
public class ModelController {
    private final ModelService modelService;
    private final MakeService makeService;

    /**
     * Constructor with dependency injection
     */
    @Autowired
    public ModelController(ModelService modelService, MakeService makeService){
        this.makeService = makeService;
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
     * Takes a model DTO and inserts a model into our database
     * @param modelDto DTO for object of type Model
     * @return The Model object that is saved
     */
    @PostMapping
    public @ResponseBody
    Model createModel(@RequestBody ModelDTO modelDto){
        Model model = modelDto.convertToEntity(makeService);
        return modelService.saveModel(model);
    }

    /**
     * Takes an array of model DTOs and inserts models into our database
     * @param modelDtos Array of Model DTOs
     * @return OK Response
     */
    @PostMapping("/multiple")
    public @ResponseBody
    ResponseEntity<String> createModel(@RequestBody ModelDTO[] modelDtos){
        List<Model> results = new ArrayList<>();
        for (ModelDTO dto : modelDtos) {
            results.add(modelService.saveModel(dto.convertToEntity(makeService)));
        }
        return ResponseEntity.ok().build();
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
