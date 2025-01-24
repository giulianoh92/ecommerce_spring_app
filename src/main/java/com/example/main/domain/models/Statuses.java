package com.example.main.domain.models;
import jakarta.persistence.*;

@Entity
@Table(name = "statuses")
public class Statuses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    public Statuses() {
    }

    public Statuses(String name) {
        this.name = name;
    }

    public Statuses(Long id, String name) {
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
