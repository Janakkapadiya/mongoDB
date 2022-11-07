package com.MongoDb.exp.MongoDb.demo.controller;

import com.MongoDb.exp.MongoDb.demo.model.Person;
import com.MongoDb.exp.MongoDb.demo.service.impl.PersonServiceImpl;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.util.List;

@RestController
public class PersonController{

    @Autowired
    private PersonServiceImpl personService;

    @PostMapping("/save")
    public Person savePersonData(@RequestBody Person person)
    {
       return personService.save(person);
    }

    @GetMapping("/findAll")
    public List<Person> findAllPerson()
    {
       return personService.findAll();
    }

    @GetMapping("/getPersonByName/{name}")
    public List<Person> getPersonByName(@PathVariable(value = "name") String name)
    {
        return personService.getPersonByPersonName(name);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePerson(@PathVariable String id)
    {
        personService.deletePersonById(id);
    }

    @GetMapping("/age")
    public List<Person> getPersonFromAgeBetween(@RequestParam Integer minAge,@RequestParam Integer maxAge)
    {
        return personService.findPersonFromAgeBetween(minAge,maxAge);
    }

    @GetMapping("/search")
    public Page<Person> searchPerson(@RequestParam(required = false) String name,
                                     @RequestParam(required = false) Integer minAge,
                                     @RequestParam(required = false) Integer maxAge,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "5") Integer size
                                     )
    {
        Pageable pageable = PageRequest.of(page,size);
        return personService.searchPerson(name,minAge,maxAge,pageable);
    }

    @GetMapping("/findByAge/{age}")
    public List<Person> findPersonByAge(@PathVariable int age)
    {
        return personService.searchByAge(age);
    }

    @GetMapping("/findByAgeWithQuery/{age}")
    public List<Person> findPersonByAgeWithQuery(@PathVariable int age)
    {
        return personService.findPersonByAgeWithQuery(age);
    }

    @GetMapping("/findOldestPersonByCity")
    public List<Document> findPersonByCity()
    {
       return personService.finOldestPersonByCity();
    }

    @GetMapping("/findCountOfPopulation")
    public List<Document> findCountOfPopulation()
    {
        return personService.findCountOfPopulation();
    }
}
