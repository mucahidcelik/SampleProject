package com.example.SampleProject.dtos;

import com.example.SampleProject.entities.Customer;

public class CustomerDto {
    private long customerId;
    private String name;
    private String address;
    private String token;

    public CustomerDto(Customer customer) {
        this.customerId = customer.getCustomerId();
        this.name = customer.getName();
        this.address = customer.getAddress();
        this.token = customer.getToken();
    }

    public CustomerDto() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
