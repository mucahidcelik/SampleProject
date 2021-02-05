package com.example.SampleProject.repositories;

import com.example.SampleProject.entities.Customer;
import com.example.SampleProject.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByToken(String token);

    @Query("select c from Customer c left join Order o on o.customer.customerId = c.customerId join OrderItem oi on oi.item = :item group by c.customerId")
    List<Customer> findByItem(@Param("item") Item item);
}
