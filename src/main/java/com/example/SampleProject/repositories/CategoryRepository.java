package com.example.SampleProject.repositories;

import com.example.SampleProject.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
