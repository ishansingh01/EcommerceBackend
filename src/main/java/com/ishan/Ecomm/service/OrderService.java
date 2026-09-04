package com.ishan.Ecomm.service;

import com.ishan.Ecomm.dto.OrderDTO;
import com.ishan.Ecomm.dto.OrderItemDTO;
import com.ishan.Ecomm.model.OrderItem;
import com.ishan.Ecomm.model.Orders;
import com.ishan.Ecomm.model.Product;
import com.ishan.Ecomm.model.User;
import com.ishan.Ecomm.repo.OrderRepository;
import com.ishan.Ecomm.repo.ProductRepository;
import com.ishan.Ecomm.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    public OrderDTO placeOrder(String email, Map<Long, Integer> productQuantities, double totalAmount) {
        User user = userRepository.findByEmail(email);
        if(user==null){
            throw new RuntimeException("User Not Found");
        }

        Orders order = new Orders();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus("Pending");
        order.setTotalAmount(totalAmount);

        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
            Product product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(entry.getValue());
            orderItems.add(orderItem);
            orderItemDTOS.add(new OrderItemDTO(product.getName(), product.getPrice(), entry.getValue()));

        }

        order.setOrderItems(orderItems);
        Orders saveOrder = orderRepository.save(order);
        return new OrderDTO(saveOrder.getId(), saveOrder.getTotalAmount(), saveOrder.getStatus(),
                saveOrder.getOrderDate(), orderItemDTOS);
    }

    public List<OrderDTO> getAllOrder() {
        List<Orders> orders = orderRepository.findAllOrdersWithUsers();
        return orders.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private OrderDTO convertToDTO(Orders orders) {
        List<OrderItemDTO> orderItem = orders.getOrderItems().stream()
                .map(item -> new OrderItemDTO(
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity()))
                .collect(Collectors.toList());
        return new OrderDTO(orders.getId(),
                orders.getTotalAmount(),
                orders.getStatus(),
                orders.getOrderDate(),
                orders.getUser() != null ? orders.getUser().getName() : "Unknown",
                orders.getUser() != null ? orders.getUser().getEmail() : "Unknown",
                orderItem);
    }

    public List<OrderDTO> getOrderByUser(String email) {
        // Optional<User> userOp = userRepository.findById(userId);
        // if (userOp.isEmpty()) {
        //     throw new RuntimeException("User Not Found");
        // }
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User Not Found");
        }
        List<Orders> ordersList = orderRepository.findByUser(user);
        return ordersList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
