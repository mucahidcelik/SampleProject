package com.example.SampleProject.services;

import com.example.SampleProject.entities.Order;
import com.example.SampleProject.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orders;

    public Order save(Order order) {
        return orders.save(order);
    }

    public Order changeOrderAddress(Long orderId, String newAddress, String token) {
        Order o = orders.findById(orderId).orElse(null);
        if (o != null) {
            if (o.getCustomer().getToken().equals(token)) {
                o.setAddress(newAddress);
                o = orders.save(o);
                return o;
            }
        }
        return null;
    }

    public Order cancelOrder(Long orderId, String token) {
        Order o = orders.findById(orderId).orElse(null);
        if (o != null) {
            if (o.getCustomer().getToken().equals(token)) {
                orders.delete(o);
                return o;
            }
        }
        return null;
    }
}
