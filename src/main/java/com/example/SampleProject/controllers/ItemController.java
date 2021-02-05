package com.example.SampleProject.controllers;

import com.example.SampleProject.entities.Item;
import com.example.SampleProject.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("/addItem")
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        item = itemService.addItem(item);
        return new ResponseEntity<>(item, item != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/getAllItems")
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> allItems = itemService.getAllItems();
        return new ResponseEntity<>(allItems, allItems != null && !allItems.isEmpty()
                ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/deleteItem/{id}")
    public ResponseEntity<Item> deleteItem(@PathVariable Long id) {
        Item item = itemService.deleteItem(id);
        return new ResponseEntity<>(item, item != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
