package com.example.SampleProject.repositories;

import com.example.SampleProject.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
