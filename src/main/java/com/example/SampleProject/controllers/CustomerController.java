package com.example.SampleProject.controllers;

import com.example.SampleProject.entities.Cart;
import com.example.SampleProject.entities.CartItem;
import com.example.SampleProject.entities.Customer;
import com.example.SampleProject.entities.Item;
import com.example.SampleProject.repositories.CartItemRepository;
import com.example.SampleProject.repositories.CartRepository;
import com.example.SampleProject.repositories.CustomerRepository;
import com.example.SampleProject.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CustomerController {
    @Autowired
    private CustomerRepository customers;
    @Autowired
    private CartItemRepository cartItems;
    @Autowired
    private CartRepository carts;
    @Autowired
    private ItemRepository items;

    @RequestMapping("/getAllCustomers")
    public List<Customer> getAllCustomers() {
        return customers.findAll();
    }

    @RequestMapping("/addCustomer")
    public Customer addCustomer(@RequestBody Customer customer) {
        Customer customerSaveResult = customers.save(customer);
        Cart cartSaveResult = carts.save(new Cart(customerSaveResult));
        customerSaveResult.setCart(cartSaveResult);
        customers.save(customerSaveResult);
        return customers.findById(customerSaveResult.getCustomerId()).orElse(null);
    }

    @RequestMapping("/getCustomer/{id}")
    public Customer getCustomer(@PathVariable Long id) {

        return customers.getOne(id);
    }

    @RequestMapping("/deleteCustomer/{id}")
    public Customer deleteCustomer(@PathVariable Long id) {
        Optional<Customer> opt = customers.findById(id);
        opt.ifPresent(customer -> customers.delete(customer));
        return opt.orElse(null);
    }

    @RequestMapping("/addItemToCart")
    public Cart deleteCustomer(@RequestParam Long itemId, @RequestParam Long cartId, @RequestParam int quantity) {
        Optional<Cart> optCart = carts.findById(cartId);
        if(optCart.isPresent()) {
            Cart cart = optCart.get();
            Optional<Item> optItem = items.findById(itemId);
            if (optItem.isPresent()) {
                CartItem ci = new CartItem(optItem.get(), cart, quantity);
                CartItem saveResult = cartItems.save(ci);
                cart.getCartItemSet().add(saveResult);
                carts.save(cart);
                return cart;
            }
        }
        return null;
    }
}
