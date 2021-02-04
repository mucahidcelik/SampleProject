package com.example.SampleProject.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long orderId;
    private String orderNumber;
    private boolean currentOrder;
    private boolean latestOrder;
    private String address;
    private String paymentMethod;

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isLatestOrder() {
        return latestOrder;
    }

    public void setLatestOrder(boolean latestOrder) {
        this.latestOrder = latestOrder;
    }

    public void setOrderItemSet(Set<OrderItem> orderItemSet) {
        this.orderItemSet = orderItemSet;
    }

    public boolean isCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(boolean lastOrder) {
        this.currentOrder = lastOrder;
    }


    public Set<OrderItem> getOrderItemSet() {
        return orderItemSet;
    }



    @ManyToOne
    @JsonBackReference(value = "customer")
    private Customer customer;
    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference(value = "order")
    private Set<OrderItem> orderItemSet;


    public Order(Customer customer) {
        this.customer = customer;
    }

    public Order() {
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
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

    public void addOrderItem(Item item, int quantity) {
        OrderItem oi = new OrderItem();
        oi.setItem(item);
        oi.setOrder(this);
        oi.setQuantity(quantity);
        getOrderItemSet().add(oi);
    }
}
