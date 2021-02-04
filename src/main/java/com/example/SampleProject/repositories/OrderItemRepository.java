package com.example.SampleProject.repositories;

import com.example.SampleProject.entities.Order;
import com.example.SampleProject.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);
}
