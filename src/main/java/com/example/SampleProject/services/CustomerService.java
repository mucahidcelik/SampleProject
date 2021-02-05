package com.example.SampleProject.services;

import com.example.SampleProject.dtos.CustomerDto;
import com.example.SampleProject.dtos.LoginDto;
import com.example.SampleProject.entities.*;
import com.example.SampleProject.repositories.CustomerRepository;
import com.example.SampleProject.security.TokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customers;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private ItemService itemService;

    public CustomerDto login(LoginDto loginDto) {
        Customer customer = customers.findById(loginDto.getId()).orElse(null);
        if (customer != null && customer.getPassword().equals(loginDto.getPassword())) {
            customer.setToken(TokenUtility.getJWTToken(customer.getName()));
            customer.setLoggedIn(true);
            customers.save(customer);
            return new CustomerDto(customer);
        }
        return null;
    }

    public CustomerDto getCustomerByToken(String token) {
        return new CustomerDto(customers.findByToken(token));
    }

    public CustomerDto register(Customer customer) {
        customer.setToken(TokenUtility.getJWTToken(customer.getName()));
        customer = customers.save(customer);
        Cart cartSaveResult = cartService.save(new Cart(customer));
        customer.setCart(cartSaveResult);
        customer.setOrders(new HashSet<>());
        Order orderSaveResult = orderService.save(new Order(customer));
        customer.addOrder(orderSaveResult);
        customer = customers.save(customer);
        return new CustomerDto(customer);
    }

    public CustomerDto delete(String token) {
        Customer customer = customers.findByToken(token);
        if (customer != null) {
            for (CartItem ci : cartItemService.findByCart(cartService.getCart(customer))) {
                ci.setCart(null);
                cartItemService.delete(ci);
            }
            cartService.getCart(customer).setCartItemSet(null);
            customers.save(customer);
            customers.delete(customers.findById(customer.getCustomerId()).orElse(null));
            return new CustomerDto(customer);
        }
        return null;
    }

    public Customer getCustomerById(Long id) {
        return customers.findById(id).orElse(null);
    }

    public void save(CustomerDto customerDto) {
        Customer customer = getCustomerById(customerDto.getCustomerId());
        if (customer != null) {
            customer.setName(customerDto.getName());
            customer.setAddress(customerDto.getAddress());
            customer.setToken(customerDto.getToken());
            customers.save(customer);
        }
    }

    public void save(Customer customer) {
        customers.save(customer);
    }

    public CustomerDto getCustomerInfo(String token) {
        Customer customer = customers.findByToken(token);
        return new CustomerDto(customer);
    }

    public Cart addItemToCart(Long itemId, int quantity, String token) {
        Customer customer = customers.findByToken(token);

        if (customer != null) {
            Cart cart = customer.getCart();
            Item item = itemService.findById(itemId);
            if (item != null) {
                CartItem ci = new CartItem(item, cart, quantity);
                CartItem saveResult = cartItemService.save(ci);
                cart.getCartItemSet().add(saveResult);
                cartService.save(cart);
                return cart;
            }
        }
        return null;
    }

    public CustomerDto editCustomer(Customer customer, String token) {
        Customer c = customers.findById(customer.getCustomerId()).orElse(null);
        if (c != null) {
            c.setAddress(customer.getAddress());
            c.setName(customer.getName());
            c.setPassword(customer.getPassword());
            Customer saveResult = customers.save(c);
            return new CustomerDto(saveResult);
        }
        return null;
    }

    public Set<Order> getCustomersOrders(String token) {
        Customer customer = customers.findByToken(token);
        return customer != null ? customer.getOrders().stream().filter(order -> !order.isCurrentOrder())
                .collect(Collectors.toSet()) : null;
    }

    public Order placeOrder(String paymentMethod, String token) {
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
                return o;
            }
        }
        return null;
    }
}
