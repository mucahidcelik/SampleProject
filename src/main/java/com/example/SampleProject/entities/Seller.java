package com.example.SampleProject.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "sellers")
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String password;
    private String token;

    public Seller() {
    }

    public Seller(String name, String password, String token, boolean loggedIn, Set<Item> itemSet) {
        this.name = name;
        this.password = password;
        this.token = token;
        this.loggedIn = loggedIn;
        this.itemSet = itemSet;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    private boolean loggedIn;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference(value = "seller")
    private Set<Item> itemSet;



    public Set<Item> getItemSet() {
        return itemSet;
    }

    public void setItemSet(Set<Item> itemSet) {
        this.itemSet = itemSet;
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
