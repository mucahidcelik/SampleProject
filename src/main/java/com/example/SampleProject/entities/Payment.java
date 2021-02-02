package com.example.SampleProject.entities;

import javax.persistence.*;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
}
