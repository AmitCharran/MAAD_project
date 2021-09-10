package com.revature.maadcars.controllers;

import com.revature.maadcars.models.Make;
import com.revature.maadcars.services.MakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("makes")
public class MakeController {
    private final MakeService makeService;

    /**
     * Constructor with dependency injection
     */
    @Autowired
    public MakeController(MakeService makeService){
        this.makeService = makeService;
    }

    /**
     * return a list of all objects in the table
     * @return a list of Makes
     */
    @GetMapping
    public @ResponseBody
    List<Make> getAllMakes(){
        return makeService.getAllMakes();
    }

    /**
     * Takes an integer and returns a Make object if
     * @param id primary key of the current object
     * @return return make object otherwise RuntimeException
     */
    @GetMapping("/{id}") // /makes/9
    public @ResponseBody
    Make findMakeById(@PathVariable String id){
        return makeService.getMakeByMakeId(Integer.parseInt(id));
    }

    /**
     * Takes a make object and inserts it into our database
     * @param make Object of type Make
     * @return The Make object that is saved
     */
    @PostMapping
    public @ResponseBody
    Make createMake(@RequestBody Make make){
        return makeService.saveMake(make);
    }

    /**
     * Takes an array of make objects and inserts it into our database
     * @param makes Array of type Make
     * @return OK Response
     */
    @PostMapping("/multiple")
    public @ResponseBody
    ResponseEntity<String> createMake(@RequestBody Make[] makes){
        List<Make> results = new ArrayList<>();
        for (Make m : makes) {
            results.add(makeService.saveMake(m));
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Takes a make object's id and changes the values of it in our database.
     * If the object does not exists, create it.
     * @param make Object of type Bid
     * @return The Make object that is saved
     */
    @PutMapping
    public @ResponseBody
    Make updateMake(@RequestBody Make make){
        return makeService.saveMake(make);
    }

    /**
     * delete a row from the Table make depending on id.
     * id is String but then parsed to an Integer.
     * @param id identifies row from table
     * @return ok status
     */
    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity<HttpStatus> deleteMake(@PathVariable String id){
        makeService.deleteMake(Integer.parseInt(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
