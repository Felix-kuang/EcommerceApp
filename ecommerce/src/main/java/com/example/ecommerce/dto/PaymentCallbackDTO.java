package com.example.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentCallbackDTO {
    private Long orderId;
    private String status;
    private String transactionId;
}
