package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoveFromCartDTO {
    @NotBlank
    String username;
    @NotBlank
    Long productId;
}
