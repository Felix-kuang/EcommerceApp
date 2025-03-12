package com.example.ecommerce.dto;

import java.util.List;

import lombok.Data;

@Data
public class CartDTO {
    private String userName;
    private Long cartId;
    private List<CartItemsDTO> items;
    private double totalPrice;
}
