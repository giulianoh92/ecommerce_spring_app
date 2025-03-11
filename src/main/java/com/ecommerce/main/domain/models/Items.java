package com.ecommerce.main.domain.models;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = true)
    @JsonBackReference
    private Orders order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id", nullable = true)
    @JsonBackReference
    private Carts cart;

    public Items() {
    }

    public Items(Products product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Items(Integer quantity, Orders order, Products product, Carts cart) {
        this.quantity = quantity;
        this.order = order;
        this.product = product;
        this.cart = cart;
    }

    public Items(Long id, Integer quantity, Orders order, Products product, Carts cart) {
        this.id = id;
        this.quantity = quantity;
        this.order = order;
        this.product = product;
        this.cart = cart;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public Carts getCart() {
        return cart;
    }

    public void setCart(Carts cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        return "Items{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", order=" + order +
                ", product=" + product +
                ", cart=" + cart +
                '}';
    }
}