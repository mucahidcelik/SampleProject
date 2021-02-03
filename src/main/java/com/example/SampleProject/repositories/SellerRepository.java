package com.example.SampleProject.repositories;

import com.example.SampleProject.entities.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller,Long> {
    Seller findByName(String name);
    Seller findByToken(String token);
}
