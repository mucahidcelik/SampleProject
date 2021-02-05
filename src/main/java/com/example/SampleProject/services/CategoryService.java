package com.example.SampleProject.services;

import com.example.SampleProject.entities.Category;
import com.example.SampleProject.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categories;

    public Category addCategory(String name) {
        Category category = new Category();
        category.setName(name);
        category.setItems(new HashSet<>());
        category=categories.save(category);
        return category;
    }

    public Category getCategoryById(Long id) {
        return categories.findById(id).orElse(null);
    }

    public List<Category> getAllCategories() {
        return categories.findAll();
    }

    public Category deleteCategory(Long id) {
        Category category = categories.findById(id).orElse(null);
        if (category != null) {
            categories.delete(category);
            return category;
        }
        return null;
    }

    public Category findById(long id) {
        return categories.findById(id).orElse(null);
    }

    public void save(Category category) {
        categories.save(category);
    }
}
