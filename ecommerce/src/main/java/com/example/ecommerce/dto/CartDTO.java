package com.example.ecommerce.dto;

import java.util.List;

import lombok.Data;

@Data
public class CartDTO {
    private Long cartId;
    private List<CartItemDTO> items;
    private double totalPrice;
}
