package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class CartItemsDTO {
    private Long productId;
    private String productName;
    private int quantity;
    private Double price;
}
