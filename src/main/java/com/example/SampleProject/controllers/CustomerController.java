package com.example.SampleProject.controllers;

import com.example.SampleProject.entities.Customer;
import com.example.SampleProject.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {
    @Autowired
    private CustomerRepository customers;

    @RequestMapping("/getUsers")
    public List<Customer> getUsers(){

        return customers.findAll();
    }

    @RequestMapping("/addUser")
    public Customer addUser(@RequestBody Customer customer){
        return customers.save(new Customer(customer));
    }

    @RequestMapping("/getUser/{id}")
    public Customer getUser(@PathVariable Long id){

        return customers.getOne(id);
    }

}
