package com.example.SampleProject.controllers;

import com.example.SampleProject.dtos.CustomerDto;
import com.example.SampleProject.dtos.LoginDto;
import com.example.SampleProject.entities.*;
import com.example.SampleProject.services.CartService;
import com.example.SampleProject.services.CustomerService;
import com.example.SampleProject.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @RequestMapping("/login")
    public ResponseEntity<CustomerDto> login(@RequestBody LoginDto loginDto) {
        CustomerDto customerDto = customerService.login(loginDto);
        return new ResponseEntity<>(customerDto, customerDto != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/logout")
    public ResponseEntity<CustomerDto> logout(@RequestHeader(name = "Authorization") String token) {
        CustomerDto customerDto = customerService.getCustomerByToken(token);
        return new ResponseEntity<>(customerDto, customerDto != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/register")
    public ResponseEntity<CustomerDto> register(@RequestBody Customer customer) {
        CustomerDto customerDto = customerService.register(customer);
        return new ResponseEntity<>(customerDto, HttpStatus.OK);
    }

    @RequestMapping("/deleteCustomer")
    public ResponseEntity<CustomerDto> deleteCustomer(@RequestHeader(name = "Authorization") String token) {
        CustomerDto result = customerService.delete(token);
        return (result != null) ? new ResponseEntity<>(result, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/getCustomerInfo")
    public ResponseEntity<CustomerDto> getCustomerInfo(@RequestHeader(name = "Authorization") String token) {

        CustomerDto customerInfo = customerService.getCustomerInfo(token);
        return customerInfo != null ? new ResponseEntity<>(customerInfo, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/addItemToCart")
    public ResponseEntity<Cart> addItemToCart(@RequestParam Long itemId, @RequestParam int quantity, @RequestHeader(name = "Authorization") String token) {
        Cart cart = customerService.addItemToCart(itemId, quantity, token);
        return new ResponseEntity<>(cart, cart != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/editCustomer")
    public ResponseEntity<CustomerDto> editCustomer(@RequestBody Customer customer, @RequestHeader(name = "Authorization") String token) {
        CustomerDto customerDto = customerService.editCustomer(customer, token);
        return new ResponseEntity<>(customerDto, customerDto != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/emptyCart")
    public ResponseEntity<Cart> emptyCart(@RequestHeader(name = "Authorization") String token) {
        Cart cart = cartService.emptyCart(token);
        return new ResponseEntity<>(cart, cart != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/getCart")
    public ResponseEntity<Cart> getCart(@RequestHeader(name = "Authorization") String token) {
        CustomerDto customerDto = customerService.getCustomerByToken(token);
        Customer customer = customerService.getCustomerById(customerDto.getCustomerId());
        if (customer != null) {
            return new ResponseEntity<>(customer.getCart(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/placeOrder")
    public ResponseEntity<Order> placeOrder(@RequestParam String paymentMethod, @RequestHeader(name = "Authorization") String token) {
        Order o = customerService.placeOrder(paymentMethod, token);
        return new ResponseEntity<>(o, o != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/listOrders")
    public ResponseEntity<HashSet<Order>> listOrders(@RequestHeader(name = "Authorization") String token) {
        Set<Order> orders = customerService.getCustomersOrders(token);
        return new ResponseEntity(orders, orders != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/changeOrderAddress")
    public ResponseEntity<Order> changeOrderAddress(@RequestParam Long orderId, @RequestParam String newAddress, @RequestHeader(name = "Authorization") String token) {
        Order o = orderService.changeOrderAddress(orderId, newAddress, token);
        return new ResponseEntity<>(o, o != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/cancelOrder")
    public ResponseEntity<Order> cancelOrder(@RequestParam Long orderId, @RequestHeader(name = "Authorization") String token) {
        Order o = orderService.cancelOrder(orderId, token);
        return new ResponseEntity<>(o, o != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

}
