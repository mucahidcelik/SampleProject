package com.example.SampleProject.entities;

import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long orderId;
    private String orderNumber;
    @OneToOne
    private Customer customer;


    public Order() {
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Order(String orderNumber, Customer customer) {
        this.orderNumber = orderNumber;
        this.customer = customer;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }


    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long id) {
        this.orderId = id;
    }

}
