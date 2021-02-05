package com.example.SampleProject.controllers;

import com.example.SampleProject.entities.Category;
import com.example.SampleProject.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/addCategory")
    public ResponseEntity<Category> addCategory(@RequestParam String name) {
        Category category = categoryService.addCategory(name);
        return new ResponseEntity<>(category, category != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/getCategory/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return new ResponseEntity<>(category, category != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/getAllCategories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> allCategories = categoryService.getAllCategories();
        return new ResponseEntity<>(allCategories, allCategories != null && !allCategories.isEmpty()
                ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/deleteCategory/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable Long id) {
        Category category = categoryService.deleteCategory(id);
        return new ResponseEntity<>(category, category != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
