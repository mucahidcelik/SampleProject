package com.example.SampleProject.controllers;

import com.example.SampleProject.dtos.LoginDto;
import com.example.SampleProject.dtos.SellerDto;
import com.example.SampleProject.entities.*;
import com.example.SampleProject.repositories.SellerRepository;
import com.example.SampleProject.services.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SellerController {
    @Autowired
    private SellerRepository sellers;
    @Autowired
    private SellerService sellerService;

    @RequestMapping("/loginSeller")
    public ResponseEntity<SellerDto> login(@RequestBody LoginDto loginDto) {
        SellerDto sellerDto = sellerService.login(loginDto);
        return new ResponseEntity<>(sellerDto, sellerDto != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);

    }

    @RequestMapping("/logoutSeller")
    public ResponseEntity<SellerDto> logout(@RequestHeader(name = "Authorization") String token) {
        SellerDto sellerDto = sellerService.getCustomerByToken(token);
        return new ResponseEntity<>(sellerDto, sellerDto != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/registerSeller")
    public ResponseEntity<SellerDto> register(@RequestBody Seller seller) {
        SellerDto sellerDto = sellerService.register(seller);
        return new ResponseEntity<>(sellerDto, sellerDto != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/deleteSeller")
    public ResponseEntity<SellerDto> deleteSeller(@RequestHeader(name = "Authorization") String token) {
        SellerDto sellerDto = sellerService.delete(token);
        return new ResponseEntity<>(sellerDto, sellerDto!=null? HttpStatus.OK: HttpStatus.NOT_FOUND);
    }

    @RequestMapping("/getAllSellers")
    public List<Seller> getAllSellers() {
        return sellers.findAll();
    }
}
