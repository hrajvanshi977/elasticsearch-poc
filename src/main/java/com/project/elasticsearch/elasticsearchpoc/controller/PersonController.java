package com.project.elasticsearch.elasticsearchpoc.controller;

import com.project.elasticsearch.elasticsearchpoc.document.Person;
import com.project.elasticsearch.elasticsearchpoc.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/person")
public class PersonController {

    private PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public void save(@RequestBody final Person person) {
        personService.save(person);
    }

    @GetMapping("/{id}")
    public Person findById(@PathVariable final String id) throws Exception {
        Person person =  personService.findById(id);
        return person;
    }
}
