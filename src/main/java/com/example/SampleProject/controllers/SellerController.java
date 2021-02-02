package com.example.SampleProject.controllers;

import com.example.SampleProject.entities.Seller;
import com.example.SampleProject.repositories.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class SellerController {
    @Autowired
    private SellerRepository sellers;

    @RequestMapping("/addSeller")
    public Seller addSeller(@RequestBody Seller seller) {
        sellers.save(seller);
        return sellers.findByName(seller.getName());
    }

    @RequestMapping("/getSeller/{id}")
    public Seller getSeller(@PathVariable Long id) {
        Optional<Seller> opt = sellers.findById(id);
        return opt.orElse(null);
    }

    @RequestMapping("/getSellerByName/{sellerName}")
    public Seller getSellerByName(@PathVariable String sellerName) {
        return sellers.findByName(sellerName);
    }

    @RequestMapping("/getAllSellers")
    public List<Seller> getAllSellers() {
        return sellers.findAll();
    }
}
