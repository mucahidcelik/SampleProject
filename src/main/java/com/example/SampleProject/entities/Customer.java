package com.example.SampleProject.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long customerId;

    private String name;
    private String password;
    private String address;

    @OneToOne(orphanRemoval = true)
    @JsonManagedReference
    private Cart cart;

    public Customer(Customer customer) {
        this.name = customer.name;
        this.password = customer.password;
        this.address = customer.address;
    }

    public Customer() {

    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public long getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public Cart getCart() {
        return cart;
    }

}
