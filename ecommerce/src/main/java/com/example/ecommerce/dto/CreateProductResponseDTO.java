package com.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String categoryName;
}