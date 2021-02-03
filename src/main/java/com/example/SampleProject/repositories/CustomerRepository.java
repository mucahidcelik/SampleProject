package com.example.SampleProject.repositories;

import com.example.SampleProject.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByToken(String token);
}
