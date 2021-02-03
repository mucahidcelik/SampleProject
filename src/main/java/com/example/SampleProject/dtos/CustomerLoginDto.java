package com.example.SampleProject.dtos;

import org.springframework.web.bind.annotation.RequestParam;

public class CustomerLoginDto {
    private Long id;
    private String password;

    public CustomerLoginDto() {
    }

    public CustomerLoginDto(Long id, String password) {
        this.id = id;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
