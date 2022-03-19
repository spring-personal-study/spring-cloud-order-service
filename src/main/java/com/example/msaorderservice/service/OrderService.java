package com.example.msaorderservice.service;

import com.example.msaorderservice.model.OrderDto;
import com.example.msaorderservice.model.OrderEntity;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    OrderDto createOrder(OrderDto orderDetails);
    OrderDto getOrderByOrderId(String orderId);
    Iterable<OrderEntity> getOrdersByUserId(String userId);
}
