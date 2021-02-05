package com.example.SampleProject.repositories;

import com.example.SampleProject.entities.Cart;
import com.example.SampleProject.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Set<CartItem> findByCart(Cart cart);
}
