package com.ecommerce.main.services.Orders.dto;

public class OrderUpdateDTO {
    private Long statusId;

    public OrderUpdateDTO() {
    }

    public OrderUpdateDTO(Long statusId) {
        this.statusId = statusId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }
}
