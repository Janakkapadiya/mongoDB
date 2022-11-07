package com.MongoDb.exp.MongoDb.demo.repo;

import com.MongoDb.exp.MongoDb.demo.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepo extends MongoRepository<Person,String>{
   @Query(value = "{'name':{'$regex: '?0','$options':'i'}}")
   List<Person> getPersonByName(String name);
   @Query(value = "{'age' : {$gt : ?0 , $lt : ?1}}")
   List<Person> findPersonFromAgeBetween(Integer minAge, Integer maxAge);

   @Query(value = "{'age' : ?0}")
   List<Person> findPersonByAgeWithQuery(Integer age);
}
