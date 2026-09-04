package com.ishan.Ecomm.controller;

import com.ishan.Ecomm.dto.OrderDTO;
import com.ishan.Ecomm.model.OrderRequest;
import com.ishan.Ecomm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public OrderDTO placeOrder(Authentication authentication, @RequestBody OrderRequest orderRequest) {
        String email = authentication.getName();
        return orderService.placeOrder(email, orderRequest.getProductQuantities(), orderRequest.getTotalAmount());
    }

    @GetMapping("/all-orders")
    public List<OrderDTO> getAllOrder() {
        return orderService.getAllOrder();
    }

    @GetMapping("/my-orders")
    public List<OrderDTO> getOrderByUser(Authentication authentication) {
        String email = authentication.getName();
        return orderService.getOrderByUser(email);
    }
}
