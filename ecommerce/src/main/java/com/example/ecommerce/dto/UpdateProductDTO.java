package com.example.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductDTO {
    private String name;
    private String description;
    private Double price;
}
