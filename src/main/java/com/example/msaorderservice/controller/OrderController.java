package com.example.msaorderservice.controller;

import com.example.msaorderservice.model.OrderDto;
import com.example.msaorderservice.model.OrderEntity;
import com.example.msaorderservice.model.ResponseOrder;
import com.example.msaorderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order-service")
public class OrderController {

    private final Environment env;
    private final OrderService orderService;

    @GetMapping("/health_check")
    public String status(HttpServletRequest request) {
        return String.format("It's Working in User Service on Port %s", request.getServerPort());
    }


    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder orderDetails) {
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(orderDetails, orderDto);
        orderDto.setUserId(userId);
        OrderDto order = orderService.createOrder(orderDto);
        ResponseOrder responseOrder = new ResponseOrder();
        BeanUtils.copyProperties(order, responseOrder);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }


    @GetMapping("/orders1")
    public ResponseEntity<ResponseOrder> getOrderByOrderId(String orderId) {
        ResponseOrder responseOrder = new ResponseOrder();
        BeanUtils.copyProperties(orderService.getOrderByOrderId(orderId), responseOrder);

        return ResponseEntity.status(HttpStatus.OK).body(responseOrder);
    }

    @GetMapping("/orders2")
    public ResponseEntity<List<ResponseOrder>> getOrdersByUserId(String userId) {
        Iterable<OrderEntity> catalogs = orderService.getOrdersByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();

        catalogs.forEach(v -> {
            ResponseOrder responseOrder = new ResponseOrder();
            BeanUtils.copyProperties(v, responseOrder);
            result.add(responseOrder);
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
