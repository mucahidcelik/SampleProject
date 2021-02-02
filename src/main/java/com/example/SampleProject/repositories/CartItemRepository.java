package com.example.SampleProject.repositories;

import com.example.SampleProject.entities.Cart;
import com.example.SampleProject.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(Cart cart);
}
