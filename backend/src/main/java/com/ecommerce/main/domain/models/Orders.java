package com.ecommerce.main.domain.models;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Orders {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private Users user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", nullable = false)
    private Statuses status;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Items> items;


    public Orders() {
    }

    public Orders(Carts cart){
        this.totalPrice = cart.getTotal();
        this.user = cart.getUser();
        this.items = cart.getItems();
        this.status = new Statuses("Pendiente");
    }

    public Orders(Double totalPrice, Users user, List<Items> items) {
        this.totalPrice = totalPrice;
        this.user = user;
        this.items = items;
        this.status = new Statuses("Pendiente");
    }

    public Orders(Double totalPrice, Users user, List<Items> items, Statuses status) {
        this.totalPrice = totalPrice;
        this.user = user;
        this.items = items;
        this.status = status;
    }

    public Orders(Long id, Double totalPrice, Users user, List<Items> items, Statuses status) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.user = user;
        this.items = items;
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

    public Double getTotal() {
        return totalPrice;
    }

    public void setTotal(Double totalPrice) {
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

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", totalPrice=" + totalPrice +
                ", user=" + user +
                ", status=" + status +
                ", items=" + items +
                '}';
    }

}