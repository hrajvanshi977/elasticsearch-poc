package com.project.elasticsearch.elasticsearchpoc.document;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Vehicle {

    private String id;

    private String number;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
