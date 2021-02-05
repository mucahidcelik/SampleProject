package com.example.SampleProject.repositories;

import com.example.SampleProject.entities.Cart;
import com.example.SampleProject.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByCustomer(Customer customer);
}
