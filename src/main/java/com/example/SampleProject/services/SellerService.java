package com.example.SampleProject.services;

import com.example.SampleProject.dtos.LoginDto;
import com.example.SampleProject.dtos.SellerDto;
import com.example.SampleProject.entities.Seller;
import com.example.SampleProject.repositories.SellerRepository;
import com.example.SampleProject.security.TokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class SellerService {
    @Autowired
    private SellerRepository sellers;
    @Autowired
    private ItemService itemService;

    public SellerDto login(LoginDto loginDto) {
        Seller seller = sellers.findById(loginDto.getId()).orElse(null);
        if (seller != null && seller.getPassword().equals(loginDto.getPassword())) {
            seller.setToken(TokenUtility.getJWTToken(seller.getName()));
            seller.setLoggedIn(true);
            sellers.save(seller);
            return new SellerDto(seller);
        }
        return null;
    }

    public SellerDto getCustomerByToken(String token) {
        Seller seller = sellers.findByToken(token);
        if (seller != null) {
            seller.setLoggedIn(false);
            sellers.save(seller);
            return new SellerDto(seller);
        } else {
            return null;
        }
    }

    public SellerDto register(Seller seller) {
        seller.setToken(TokenUtility.getJWTToken(seller.getName()));
        seller.setLoggedIn(true);
        seller.setItemSet(new HashSet<>());
        Seller sellerSaveResult = sellers.save(seller);
        return new SellerDto(sellerSaveResult);
    }

    public SellerDto delete(String token) {
        Seller seller = sellers.findByToken(token);
        if (seller != null) {
            itemService.deleteItemsOfSeller(seller);
            seller.setItemSet(new HashSet<>());
            seller = sellers.save(seller);
            sellers.delete(seller);
            return new SellerDto(seller);
        }
        return null;
    }
}
