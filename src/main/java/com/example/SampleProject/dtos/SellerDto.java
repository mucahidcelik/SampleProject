package com.example.SampleProject.dtos;

import com.example.SampleProject.entities.Item;
import com.example.SampleProject.entities.Seller;

import java.util.Set;

public class SellerDto {
    private long id;
    private String name;
    private String token;
    private Set<Item> itemSet;

    public SellerDto() {
    }

    public SellerDto(Seller seller) {
        this.id = seller.getId();
        this.name = seller.getName();
        this.token = seller.getToken();
        this.itemSet = seller.getItemSet();
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


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<Item> getItemSet() {
        return itemSet;
    }

    public void setItemSet(Set<Item> itemSet) {
        this.itemSet = itemSet;
    }
}
