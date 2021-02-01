package com.example.SampleProject.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    private long id;

    @OneToOne
    private Customer customer;

}
