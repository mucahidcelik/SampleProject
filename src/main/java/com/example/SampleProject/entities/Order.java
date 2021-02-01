package com.example.SampleProject.entities;

import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    private long orderId;
    private String orderNumber;



    public Order() {
    }

    public Order(String orderNumber, Customer customer) {
        this.orderNumber = orderNumber;
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
