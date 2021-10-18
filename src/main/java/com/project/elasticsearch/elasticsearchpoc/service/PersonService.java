package com.project.elasticsearch.elasticsearchpoc.service;

import com.project.elasticsearch.elasticsearchpoc.document.Person;
import com.project.elasticsearch.elasticsearchpoc.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {

    private PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    public void save(final Person person) {
            personRepository.save(person);
    }

    public Person findById(final String id) throws Exception {
        Optional<Person> person =  personRepository.findById(id);

        if(!person.isPresent()) {
            throw new Exception("User not found");
        }
        return person.get();
    }
}
