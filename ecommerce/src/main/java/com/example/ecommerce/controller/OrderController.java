package com.example.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.service.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<OrderDTO> checkout(@RequestParam String username) {

        // Cek apakah user yang checkout sama dengan user yang login
        String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Logged in user: " + loggedInUser);
        System.out.println("Username param: " + username);
        if (!loggedInUser.equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // Lanjut proses checkout
        OrderDTO orderDTO = orderService.checkout(username);
        return ResponseEntity.ok(orderDTO);
    }
}
