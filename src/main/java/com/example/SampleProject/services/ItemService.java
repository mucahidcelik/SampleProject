package com.example.SampleProject.services;

import com.example.SampleProject.entities.Category;
import com.example.SampleProject.entities.Item;
import com.example.SampleProject.entities.Seller;
import com.example.SampleProject.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    private ItemRepository items;
    @Autowired
    private CategoryService categoryService;


    public Item findById(Long itemId) {
        return items.findById(itemId).orElse(null);
    }

    public Item addItem(Item item) {
        Category category = categoryService.findById(item.getCategory().getId());
        if (category != null) {
            item.setCategory(category);
            item = items.save(item);
            item.getCategory().getItems().add(item);
            categoryService.save(item.getCategory());
        }
        return item;
    }

    public List<Item> getAllItems() {
        return items.findAll();
    }

    public Item deleteItem(Long id) {
        Item i = items.findById(id).orElse(null);
        if (i != null) {
            Category category = categoryService.findById(i.getCategory().getId());
            if (category != null) {
                category.getItems().remove(i);
                category.setItems(category.getItems());
                items.delete(i);
                categoryService.save(category);
                return i;
            }
        }
        return null;
    }

    public void deleteItemsOfSeller(Seller seller) {
        for (Item i : items.findBySeller(seller)) {
            i.setSeller(null);
            items.delete(i);
        }
    }
}
