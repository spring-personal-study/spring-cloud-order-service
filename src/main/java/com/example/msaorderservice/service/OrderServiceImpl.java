package com.example.msaorderservice.service;

import com.example.msaorderservice.model.OrderDto;
import com.example.msaorderservice.model.OrderEntity;
import com.example.msaorderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getUnitPrice() * orderDto.getQty());
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(orderDto, orderEntity);
        orderRepository.save(orderEntity);

        OrderDto returnOrderDto = new OrderDto();
        BeanUtils.copyProperties(orderEntity, returnOrderDto);

        return returnOrderDto;
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        OrderEntity userEntity = orderRepository.findByOrderId(orderId);
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(userEntity, orderDto);

        return orderDto;
    }

    @Override
    public Iterable<OrderEntity> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
}
