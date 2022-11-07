package com.MongoDb.exp.MongoDb.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "person")
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person {

    @Id
    private String id;
    private String name;
    private Integer age;
    private List<Address> address;
    private List<String> hobbies;

}
