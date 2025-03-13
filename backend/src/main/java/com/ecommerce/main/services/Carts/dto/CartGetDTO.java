package com.ecommerce.main.services.Carts.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.ecommerce.main.domain.models.Carts;

public class CartGetDTO {
    private long id;
    private List<Item> items;

    public static class Item {
        private long id;
        private String name;
        private double unitPrice;
        private int quantity;
        private double total;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }
    }
    private double total;

    public CartGetDTO(long id, List<Item> items, double total) {
        this.id = id;
        this.items = items;
        this.total = total;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public static CartGetDTO mapToDto(Carts cart) {
        List<Item> items = cart.getItems().stream().map(item -> {
            Item i = new Item();
            i.setId(item.getProduct().getId());
            i.setName(item.getProduct().getName());
            i.setUnitPrice(item.getProduct().getUnitPrice());
            i.setQuantity(item.getQuantity());
            i.setTotal(item.getProduct().getUnitPrice() * item.getQuantity());
            return i;
        }).sorted((i1, i2) -> Long.compare(i1.getId(), i2.getId())).collect(Collectors.toList());
        double total = items.stream().mapToDouble(i -> i.getTotal()).sum();
        return new CartGetDTO(cart.getId(), items, total);
    }

}
