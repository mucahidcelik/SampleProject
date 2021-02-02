package com.example.SampleProject.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private double price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    @JsonBackReference
    private Category category;



    public Item(String name, double price, Category category) {
        this.name = name;
        this.price =price;
        this.category = category;
    }

    public Item() {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
