package com.MongoDb.exp.MongoDb.demo.service;

import com.MongoDb.exp.MongoDb.demo.model.Person;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PersonService {

    Person save(Person person);

    List<Person> getPersonByPersonName(String name);

    void deletePersonById(String id);

    List<Person> findPersonFromAgeBetween(Integer minAge, Integer maxAge);

    Page<Person> searchPerson(String name,Integer minAge,Integer maxAge,Pageable pageable);

    List<Person> findAll();

    List<Person> searchByAge(int age);

    List<Person> findPersonByAgeWithQuery(int age);

    List<Document> finOldestPersonByCity();

    List<Document> findCountOfPopulation();
}
