package com.ecommerce.main.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    public Categories() {
    }

    public Categories(String name) {
        this.name = name;
    }

    public Categories(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
