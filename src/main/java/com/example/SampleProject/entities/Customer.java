package com.example.SampleProject.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long customerId;

    private String name;
    @JsonProperty
    private String password;
    private String address;
    private String token;
    private boolean loggedIn;
    @OneToOne(orphanRemoval = true)
    @JsonManagedReference
    private Cart cart;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "customer")
    private Set<Order> orders;


    public Customer() {

    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @JsonIgnore
    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    @JsonIgnore
    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Order getCurrentOrder() {
        for (Order o : orders) {
            if (o.isCurrentOrder()) {
                return o;
            }
        }
        return null;
    }

    public Order getLatestOrder() {
        for (Order o : orders) {
            if (o.isLatestOrder()) {
                return o;
            }
        }
        return null;
    }

    public void addOrder(Order order) {
        Order current = getCurrentOrder();
        Order latest = getLatestOrder();
        if (current != null) {
            current.setCurrentOrder(false);
            current.setLatestOrder(true);
        }
        if (latest != null) {
            latest.setLatestOrder(false);
        }
        order.setCurrentOrder(true);
        order.setLatestOrder(false);
        getOrders().add(order);
    }

}
