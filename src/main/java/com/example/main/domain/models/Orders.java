package com.example.main.domain.models;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Orders {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private Statuses status;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<Items> items;

    public Orders() {
    }

    public Orders(Double totalPrice, Users user, Statuses status) {
        this.totalPrice = totalPrice;
        this.user = user;
        this.status = status;
    }

    public Orders(Long id, Double totalPrice, Users user, Statuses status) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.user = user;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Statuses getStatus() {
        return status;
    }

    public void setStatus(Statuses status) {
        this.status = status;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

}