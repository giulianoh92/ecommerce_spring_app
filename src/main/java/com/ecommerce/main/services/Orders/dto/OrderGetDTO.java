package com.ecommerce.main.services.Orders.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.ecommerce.main.domain.models.Orders;

public class OrderGetDTO {
    private long id;
    private long userId;
    private List<Item> items;
    private String status;

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

    public OrderGetDTO(long id, long userId, List<Item> items, double total, String status) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.total = total;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static OrderGetDTO mapToDto(Orders order) {
        List<Item> items = order.getItems().stream().map(item -> {
            Item i = new Item();
            i.setId(item.getProduct().getId());
            i.setName(item.getProduct().getName());
            i.setUnitPrice(item.getProduct().getUnitPrice());
            i.setQuantity(item.getQuantity());
            i.setTotal(item.getProduct().getUnitPrice() * item.getQuantity());
            return i;
        }).collect(Collectors.toList());
        double total = items.stream().mapToDouble(i -> i.getTotal()).sum();
        return new OrderGetDTO(order.getId(), order.getUser().getId(), items, total, order.getStatus().getName());
    }

}
