package com.example.SampleProject.services;

import com.example.SampleProject.dtos.CustomerDto;
import com.example.SampleProject.entities.Cart;
import com.example.SampleProject.entities.CartItem;
import com.example.SampleProject.entities.Customer;
import com.example.SampleProject.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CartService {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CartRepository carts;

    public Cart getCart(Customer customer) {
        return carts.findByCustomer(customer);
    }

    public Cart save(Cart cart) {
        return carts.save(cart);
    }


    public Cart emptyCart(String token) {
        CustomerDto customerDto = customerService.getCustomerByToken(token);
        if (customerDto != null) {
            Customer customer = customerService.getCustomerById(customerDto.getCustomerId());
            if (customer != null) {
                carts.delete(customer.getCart());
                Cart cartSaveResult = carts.save(new Cart(customer));
                customer.setCart(cartSaveResult);
                customerService.save(customer);
                return cartSaveResult;
            }
        }
        return null;
    }
}
