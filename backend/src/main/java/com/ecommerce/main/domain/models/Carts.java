package com.ecommerce.main.domain.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "carts")
public class Carts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Users user;

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY)
    @JsonManagedReference
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

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public void addItem(Items item) {
        items.add(item);
    }

    public void removeItem(Items item) {
        items.remove(item);
    }

    public void updateItem(Items item, int quantity) {
        items.stream()
            .filter(i -> i.getProduct().getId() == item.getProduct().getId())
            .findFirst()
            .ifPresent(i -> i.setQuantity(quantity));
    }

    public void clearItems() {
        items.clear();
    }

    public double getTotal() {
        return items.stream()
            .mapToDouble(item -> item.getProduct().getUnitPrice() * item.getQuantity())
            .sum();
    }

    @Override
    public String toString() {
        return "Carts [id=" + id + ", user=" + user + ", items=" + items + "]";
    }
}