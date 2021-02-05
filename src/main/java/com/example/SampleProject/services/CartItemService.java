package com.example.SampleProject.services;

import com.example.SampleProject.entities.Cart;
import com.example.SampleProject.entities.CartItem;
import com.example.SampleProject.repositories.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepository cartItems;

    public Set<CartItem> findByCart(Cart cart) {
        return cartItems.findByCart(cart);
    }

    public void delete(CartItem ci) {
        cartItems.delete(ci);
    }

    public CartItem save(CartItem ci) {
        return cartItems.save(ci);
    }
}
