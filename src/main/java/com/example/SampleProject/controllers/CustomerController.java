package com.example.SampleProject.controllers;

import com.example.SampleProject.dtos.CustomerDto;
import com.example.SampleProject.dtos.LoginDto;
import com.example.SampleProject.entities.*;
import com.example.SampleProject.repositories.*;
import com.example.SampleProject.security.TokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
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
    @Autowired
    private OrderRepository orders;

    @RequestMapping("/login")
    public ResponseEntity<CustomerDto> login(@RequestBody LoginDto loginDto) {
        Customer customer = customers.findById(loginDto.getId()).orElse(null);
        if (customer != null && customer.getPassword().equals(loginDto.getPassword())) {
            customer.setToken(TokenUtility.getJWTToken(customer.getName()));
            customer.setLoggedIn(true);
            customers.save(customer);
            return new ResponseEntity<>(new CustomerDto(customer), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping("/logout")
    public ResponseEntity<CustomerDto> logout(@RequestHeader(name = "Authorization") String token) {
        Customer customer = customers.findByToken(token);
        if (customer != null) {
            customer.setLoggedIn(false);
            customers.save(customer);
            return new ResponseEntity<>(new CustomerDto(customer), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/register")
    public ResponseEntity<CustomerDto> register(@RequestBody Customer customer) {
        customer.setToken(TokenUtility.getJWTToken(customer.getName()));
        customer = customers.save(customer);
        Cart cartSaveResult = carts.save(new Cart(customer));
        customer.setCart(cartSaveResult);
        customer.setOrders(new HashSet<>());
        Order orderSaveResult = orders.save(new Order(customer));
        customer.addOrder(orderSaveResult);
        customer = customers.save(customer);
        return new ResponseEntity<>(new CustomerDto(customer), HttpStatus.OK);
    }

    @RequestMapping("/deleteCustomer")
    public ResponseEntity<CustomerDto> deleteCustomer(@RequestHeader(name = "Authorization") String token) {
        Customer customer = customers.findByToken(token);
        if (customer != null) {
            for (CartItem ci : cartItems.findByCart(customer.getCart())) {
                ci.setCart(null);
                cartItems.delete(ci);
            }
            customer.getCart().setCartItemSet(null);
            customers.save(customer);
            customers.delete(customer);
            return new ResponseEntity<>(new CustomerDto(customer), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/getCustomerInfo")
    public ResponseEntity<CustomerDto> getCustomerInfo(@RequestHeader(name = "Authorization") String token) {
        Customer customer = customers.findByToken(token);
        return customer == null ? new ResponseEntity<>(null, HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(new CustomerDto(customer), HttpStatus.OK);
    }

    @RequestMapping("/addItemToCart")
    public ResponseEntity<Cart> addItemToCart(@RequestParam Long itemId, @RequestParam int quantity, @RequestHeader(name = "Authorization") String token) {
        Customer customer = customers.findByToken(token);
        if (customer != null) {
            Cart cart = customer.getCart();
            Optional<Item> optItem = items.findById(itemId);
            if (optItem.isPresent()) {
                CartItem ci = new CartItem(optItem.get(), cart, quantity);
                CartItem saveResult = cartItems.save(ci);
                cart.getCartItemSet().add(saveResult);
                carts.save(cart);
                return new ResponseEntity<>(cart, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping("/editCustomer")
    public ResponseEntity<CustomerDto> editCustomer(@RequestBody Customer customer, @RequestHeader(name = "Authorization") String token) {
        Customer c = customers.findById(customer.getCustomerId()).orElse(null);
        if (c != null) {
            if (c.getToken().equals(token)) {
                c.setAddress(customer.getAddress());
                c.setName(customer.getName());
                c.setPassword(customer.getPassword());
                Customer saveResult = customers.save(c);
                return new ResponseEntity<>(new CustomerDto(saveResult), HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/emptyCart")
    public ResponseEntity<Cart> emptyCart(@RequestParam Long id, @RequestHeader(name = "Authorization") String token) {
        Customer c = customers.findById(id).orElse(null);
        if (c != null) {
            if (c.getToken().equals(token)) {
                carts.delete(c.getCart());
                Cart cartSaveResult = carts.save(new Cart(c));
                c.setCart(cartSaveResult);
                customers.save(c);
                return new ResponseEntity<>(cartSaveResult, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/getCart")
    public ResponseEntity<Cart> getCart(@RequestHeader(name = "Authorization") String token) {
        Customer c = customers.findByToken(token);
        if (c != null) {
            return new ResponseEntity<>(c.getCart(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/placeOrder")
    public ResponseEntity<Order> placeOrder(@RequestParam String paymentMethod, @RequestHeader(name = "Authorization") String token) {
        Customer c = customers.findByToken(token);
        if (c != null) {
            if (c.getCart().getCartItemSet().size() != 0) {
                for (CartItem ci : c.getCart().getCartItemSet()) {
                    c.getCurrentOrder().addOrderItem(ci.getItem(), ci.getQuantity());
                    c.getCart().getCartItemSet().remove(ci);
                }
                c.getCart().setCartItemSet(new HashSet<>());
                Order o = c.getCurrentOrder();
                o.setAddress(c.getAddress());
                o.setPaymentMethod(paymentMethod);
                c.addOrder(new Order(c));
                customers.save(c);
                return new ResponseEntity<>(o, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping("/listOrders")
    public ResponseEntity<HashSet<Order>> listOrders(@RequestHeader(name = "Authorization") String token) {
        Customer customer = customers.findByToken(token);
        if (customer != null) {
            return new ResponseEntity(customer.getOrders(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping("/changeOrderAddress")
    public ResponseEntity<Order> changeOrderAddress(@RequestParam Long orderId, @RequestParam String newAddress, @RequestHeader(name = "Authorization") String token) {
        Order o = orders.findById(orderId).orElse(null);
        if (o != null) {
            if (o.getCustomer().getToken().equals(token)) {
                o.setAddress(newAddress);
                o = orders.save(o);
                return new ResponseEntity<>(o, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/cancelOrder")
    public ResponseEntity<Order> cancelOrder(@RequestParam Long orderId, @RequestHeader(name = "Authorization") String token) {
        Order o = orders.findById(orderId).orElse(null);
        if (o != null) {
            if (o.getCustomer().getToken().equals(token)) {
                orders.delete(o);
                return new ResponseEntity<>(o, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

}
