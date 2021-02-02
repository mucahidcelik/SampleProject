package com.example.SampleProject.controllers;

import com.example.SampleProject.entities.Category;
import com.example.SampleProject.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class CategoryController {
    @Autowired
    private CategoryRepository categories;

    @RequestMapping("/addCategory")
    public Category addCategory(@RequestBody Category category) {
        categories.save(category);
        return categories.findByName(category.getName());
    }

    @RequestMapping("/getCategory/{id}")
    public Category getCategory(@PathVariable Long id) {
        Optional<Category> opt = categories.findById(id);
        return opt.orElse(null);
    }

    @RequestMapping("/getAllCategories")
    public List<Category> getAllCategories() {
        return categories.findAll();
    }

    @RequestMapping("/deleteCategory/{id}")
    public Category deleteCategory(@PathVariable Long id) {
        Optional<Category> opt = categories.findById(id);
        if (opt.isPresent()) {
            categories.delete(opt.get());
            return opt.get();
        }
        return null;
    }
}
