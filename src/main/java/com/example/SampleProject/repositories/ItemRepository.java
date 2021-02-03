package com.example.SampleProject.repositories;

import com.example.SampleProject.entities.Item;
import com.example.SampleProject.entities.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByName(String name);

    List<Item> findBySeller(Seller seller);
}
