package com.example.SampleProject.repositories;

import com.example.SampleProject.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
