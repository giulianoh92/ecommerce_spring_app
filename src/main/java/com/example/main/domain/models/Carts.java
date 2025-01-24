package com.example.main.domain.models;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "carts")
public class Carts {

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY)
    private List<Items> items;

    public Carts() {
    }

    public Carts(Users user) {
        this.user = user;
    }

    public Carts(Long id, Users user) {
        this.id = id;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}