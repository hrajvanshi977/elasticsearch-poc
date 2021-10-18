package com.project.elasticsearch.elasticsearchpoc.repository;

import com.project.elasticsearch.elasticsearchpoc.document.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PersonRepository extends ElasticsearchRepository<Person, String> {
}
