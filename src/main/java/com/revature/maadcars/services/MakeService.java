package com.revature.maadcars.services;

import com.revature.maadcars.models.Make;
import com.revature.maadcars.repository.MakeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MakeService {
    private final MakeRepository makeRepository;

    @Autowired
    public MakeService(MakeRepository makeRepository){
        this.makeRepository = makeRepository;
    }
    /**
     * Takes a make object and inserts it into our database
     * @param make Object of type Make
     * @return The Make object that is saved
     */
    public Make saveMake(Make make){
        return makeRepository.save(make);
    }
    /**
     * Takes an integer and returns a Make object if
     * @param id primary key of the current object
     * @return return Make object otherwise RuntimeException
     */
    public Make getMakeByMakeId(Integer id){
        return makeRepository.findById(id).orElse(null);
    }
    /**
     * return a list of all objects in the table
     * @return a list of Makes
     */
    public List<Make> getAllMakes(){
        return makeRepository.findAll();
    }
    /**
     * delete a row from the Table make depending on id
     * @param makeId identifies row from table
     */
    public void deleteMake(Integer makeId){
        makeRepository.findById(makeId).ifPresent(makeRepository::delete);
    }
}
