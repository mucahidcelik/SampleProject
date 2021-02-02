package com.example.SampleProject.controllers;

import com.example.SampleProject.entities.Category;
import com.example.SampleProject.entities.Customer;
import com.example.SampleProject.entities.Item;
import com.example.SampleProject.repositories.CategoryRepository;
import com.example.SampleProject.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class ItemController {
    @Autowired
    private ItemRepository items;
    @Autowired
    private CategoryRepository categories;

    @RequestMapping("/addItem")
    public Item addItem(@RequestBody Item item) {
        Optional<Category> opt = categories.findById(item.getCategory().getId());
        item.setCategory(null);
        Item saveResult = items.save(item);
        if(opt.isPresent()){
            Category c = opt.get();
            saveResult.setCategory(opt.get());
            if (c.getItems().add(saveResult)) {
                c.setItems(c.getItems());
                categories.save(c);
            }
        }
        saveResult = items.save(saveResult);
        return items.findById(saveResult.getId()).orElse(null);
    }

    @RequestMapping("/getAllItems")
    public List<Item> getAllItems() {
        return items.findAll();
    }

    @RequestMapping("/deleteItem/{id}")
    public Item deleteItem(@PathVariable Long id) {
        Optional<Item> opt = items.findById(id);
        if(opt.isPresent()) {
            Item i = opt.get();
            Category category = categories.findById(i.getCategory().getId()).get();
            category.getItems().remove(i);
            category.setItems(category.getItems());
            items.delete(i);
            return i;
        }
        return null;
    }
}
