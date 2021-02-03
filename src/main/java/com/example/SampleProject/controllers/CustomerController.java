package com.example.SampleProject.controllers;

import com.example.SampleProject.dtos.CustomerDto;
import com.example.SampleProject.dtos.CustomerLoginDto;
import com.example.SampleProject.entities.Cart;
import com.example.SampleProject.entities.CartItem;
import com.example.SampleProject.entities.Customer;
import com.example.SampleProject.entities.Item;
import com.example.SampleProject.repositories.CartItemRepository;
import com.example.SampleProject.repositories.CartRepository;
import com.example.SampleProject.repositories.CustomerRepository;
import com.example.SampleProject.repositories.ItemRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @RequestMapping("/login")
    public ResponseEntity<CustomerDto> login(@RequestBody CustomerLoginDto customerDto) {
        Customer customer = customers.findById(customerDto.getId()).orElse(null);
        if (customer != null && customer.getPassword().equals(customerDto.getPassword())) {
            customer.setToken(getJWTToken(customer.getName()));
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
        customer.setToken(getJWTToken(customer.getName()));
        Customer customerSaveResult = customers.save(customer);
        Cart cartSaveResult = carts.save(new Cart(customerSaveResult));
        customerSaveResult.setCart(cartSaveResult);
        customerSaveResult = customers.save(customerSaveResult);
        return new ResponseEntity<>(new CustomerDto(customerSaveResult), HttpStatus.OK);
    }

    @RequestMapping("/deleteCustomer")
    public ResponseEntity<CustomerDto> deleteCustomer(@RequestHeader(name = "Authorization") String token) {
        Customer customer = customers.findByToken(token);
        if (customer != null) {
            for(CartItem ci : cartItems.findByCart(customer.getCart())){
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
    public ResponseEntity<Cart> addItemToCart(@RequestParam Long itemId, @RequestParam Long cartId, @RequestParam int quantity, @RequestHeader(name = "Authorization") String token) {
        Optional<Cart> optCart = carts.findById(cartId);
        if (optCart.isPresent()) {
            Cart cart = optCart.get();
            if (cart.getCustomer().getToken().equals(token) && cart.getCustomer().isLoggedIn()) {
                Optional<Item> optItem = items.findById(itemId);
                if (optItem.isPresent()) {
                    CartItem ci = new CartItem(optItem.get(), cart, quantity);
                    CartItem saveResult = cartItems.save(ci);
                    cart.getCartItemSet().add(saveResult);
                    carts.save(cart);
                    return new ResponseEntity<>(cart, HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/editCustomer")
    public ResponseEntity<Customer> editCustomer(@RequestBody Customer customer, @RequestHeader(name = "Authorization") String token){
        Customer c = customers.findById(customer.getCustomerId()).orElse(null);
        if(c!=null){
            if(c.getToken().equals(token)){
                c.setAddress(customer.getAddress());
                c.setName(customer.getName());
                c.setPassword(customer.getPassword());
                Customer saveResult = customers.save(c);
                return new ResponseEntity<>(saveResult, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/emptyCart")
    public ResponseEntity<Cart> emptyCart(@RequestParam Long id, @RequestHeader(name = "Authorization") String token){
        Customer c = customers.findById(id).orElse(null);
        if(c!=null){
            if(c.getToken().equals(token)){
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

    private String getJWTToken(String username) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("sample")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer " + token;
    }
}
