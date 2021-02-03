package com.example.SampleProject.controllers;

import com.example.SampleProject.dtos.CustomerDto;
import com.example.SampleProject.dtos.LoginDto;
import com.example.SampleProject.dtos.SellerDto;
import com.example.SampleProject.entities.*;
import com.example.SampleProject.repositories.CategoryRepository;
import com.example.SampleProject.repositories.ItemRepository;
import com.example.SampleProject.repositories.SellerRepository;
import com.example.SampleProject.security.TokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

@RestController
public class SellerController {
    @Autowired
    private SellerRepository sellers;
    @Autowired
    private CategoryRepository categories;
    @Autowired
    private ItemRepository items;

    @RequestMapping("/loginSeller")
    public ResponseEntity<SellerDto> login(@RequestBody LoginDto loginDto) {
        Seller seller = sellers.findById(loginDto.getId()).orElse(null);
        if (seller != null && seller.getPassword().equals(loginDto.getPassword())) {
            seller.setToken(TokenUtility.getJWTToken(seller.getName()));
            seller.setLoggedIn(true);
            sellers.save(seller);
            return new ResponseEntity<>(new SellerDto(seller), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping("/logoutSeller")
    public ResponseEntity<SellerDto> logout(@RequestHeader(name = "Authorization") String token) {
        Seller seller = sellers.findByToken(token);
        if (seller != null) {
            seller.setLoggedIn(false);
            sellers.save(seller);
            return new ResponseEntity<>(new SellerDto(seller), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/registerSeller")
    public ResponseEntity<SellerDto> register(@RequestBody Seller seller) {
        seller.setToken(TokenUtility.getJWTToken(seller.getName()));
        seller.setLoggedIn(true);
        seller.setItemSet(new TreeSet<>());
        Seller sellerSaveResult = sellers.save(seller);
        return new ResponseEntity<>(new SellerDto(sellerSaveResult), HttpStatus.OK);
    }

    @RequestMapping("/deleteSeller")
    public ResponseEntity<SellerDto> deleteSeller(@RequestHeader(name = "Authorization") String token) {
        Seller seller = sellers.findByToken(token);
        if (seller != null) {
            for(Item i : items.findBySeller(seller)){
                i.setSeller(null);
                items.delete(i);
            }
            seller.setItemSet(null);
            sellers.save(seller);
            sellers.delete(seller);
            return new ResponseEntity<>(new SellerDto(seller), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/getAllSellers")
    public List<Seller> getAllSellers() {
        return sellers.findAll();
    }

    @RequestMapping("/addCategory")
    public Category addCategory(@RequestBody Category category) {
        categories.save(category);
        return categories.findByName(category.getName());
    }

    @RequestMapping("/getCategory/{id}")
    public Category getCategory(@PathVariable Long id) {
        Optional<Category> opt = categories.findById(id);
        return opt.orElse(null);
    }

    @RequestMapping("/getAllCategories")
    public List<Category> getAllCategories() {
        return categories.findAll();
    }

    @RequestMapping("/deleteCategory/{id}")
    public Category deleteCategory(@PathVariable Long id) {
        Optional<Category> opt = categories.findById(id);
        if (opt.isPresent()) {
            categories.delete(opt.get());
            return opt.get();
        }
        return null;
    }

    @RequestMapping("/addItem")
    public Item addItem(@RequestBody Item item) {
        Optional<Category> opt = categories.findById(item.getCategory().getId());
        item.setCategory(null);
        Item saveResult = items.save(item);
        if (opt.isPresent()) {
            Category c = opt.get();
            saveResult.setCategory(opt.get());
            if (c.getItems().add(saveResult)) {
                c.setItems(c.getItems());
                categories.save(c);
            }
        }
        saveResult = items.save(saveResult);
        return items.findById(saveResult.getId()).orElse(null);
    }

    @RequestMapping("/getAllItems")
    public List<Item> getAllItems() {
        return items.findAll();
    }

    @RequestMapping("/deleteItem/{id}")
    public Item deleteItem(@PathVariable Long id) {
        Optional<Item> opt = items.findById(id);
        if (opt.isPresent()) {
            Item i = opt.get();
            Category category = categories.findById(i.getCategory().getId()).get();
            category.getItems().remove(i);
            category.setItems(category.getItems());
            items.delete(i);
            return i;
        }
        return null;
    }
}
