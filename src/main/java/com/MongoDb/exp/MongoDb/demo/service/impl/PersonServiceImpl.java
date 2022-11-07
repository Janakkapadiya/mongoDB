package com.MongoDb.exp.MongoDb.demo.service.impl;

import com.MongoDb.exp.MongoDb.demo.model.Person;
import com.MongoDb.exp.MongoDb.demo.repo.PersonRepo;
import com.MongoDb.exp.MongoDb.demo.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    public final PersonRepo personRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Person save(Person person) {
        return personRepo.save(person);
    }

    @Override
    public List<Person> getPersonByPersonName(String name) {
        return personRepo.getPersonByName(name);
    }

    @Override
    public void deletePersonById(String id) {
        personRepo.deleteById(id);
    }

    @Override
    public List<Person> findPersonFromAgeBetween(Integer minAge, Integer maxAge) {
        return personRepo.findPersonFromAgeBetween(minAge, maxAge);
    }

    @Override
    public Page<Person> searchPerson(String name, Integer minAge, Integer maxAge, Pageable pageable) {
        Query query = new Query().with(pageable);
        List<Criteria> criteria = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            criteria.add(Criteria.where("name").regex(name, "i"));
        }

        if (minAge != null && maxAge != null) {
            criteria.add(Criteria.where("age").gte(minAge).lte(maxAge));
        }

        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }
        return PageableExecutionUtils.getPage(mongoTemplate.find(query, Person.class), pageable, () -> mongoTemplate.count(query.skip(0).limit(0), Person.class));
    }

    @Override
    public List<Person> findAll() {
        return personRepo.findAll();
    }

    @Override
    public List<Person> searchByAge(int age) {
        MatchOperation matchOperation = Aggregation.match(new Criteria("age").is(age));

        SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Direction.DESC, "age"));

        Aggregation aggregation = Aggregation.newAggregation(matchOperation, sortOperation);

        AggregationResults<Person> output = mongoTemplate.aggregate(aggregation, "person", Person.class);

        return output.getMappedResults();
    }

    @Override
    public List<Person> findPersonByAgeWithQuery(int age) {
        return personRepo.findPersonByAgeWithQuery(age);
    }

    @Override
    public List<Document> finOldestPersonByCity() {
        UnwindOperation unwindOperation = Aggregation.unwind("address");
        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "age");
        GroupOperation groupOperation = Aggregation.group("address.city").first(Aggregation.ROOT).as("oldestPerson");
        Aggregation aggregation = Aggregation.newAggregation(unwindOperation, sortOperation, groupOperation);
        return mongoTemplate.aggregate(aggregation, Person.class, Document.class).getMappedResults();
    }

    @Override
    public List<Document> findCountOfPopulation() {
        UnwindOperation unwindOperation = Aggregation.unwind("address");
        GroupOperation groupOperation = Aggregation.group("address.city").count().as("populationCount");
        SortOperation sortOperation = Aggregation.sort(Sort.Direction.DESC, "populationCount");

        ProjectionOperation projection = Aggregation.project().andExpression("_id").as("city").
                andExpression("popCount").as("count").andExclude("_id");

        Aggregation population = Aggregation.newAggregation(unwindOperation, groupOperation, sortOperation, projection);
        return mongoTemplate.aggregate(population, Person.class, Document.class).getMappedResults();
    }
}
