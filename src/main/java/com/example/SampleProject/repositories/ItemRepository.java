package com.example.SampleProject.repositories;

import com.example.SampleProject.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByName(String name);
}
