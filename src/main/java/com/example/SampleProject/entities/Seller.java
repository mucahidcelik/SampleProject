package com.example.SampleProject.entities;

import javax.persistence.*;

@Entity
@Table(name = "sellers")
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    public Seller() {
    }

    public Seller(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
