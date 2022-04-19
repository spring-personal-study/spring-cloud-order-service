package com.example.msaorderservice.controller;

import com.example.msaorderservice.messagequeue.KafkaProducer;
import com.example.msaorderservice.messagequeue.OrderProducer;
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
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;

    @GetMapping("/health_check")
    public String status(HttpServletRequest request) {
        return String.format("It's Working in User Service on Port %s", request.getServerPort());
    }


    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder orderDetails) {
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(orderDetails, orderDto);
        /* jpa */
        orderDto.setUserId(userId);
        orderDto = orderService.createOrder(orderDto);
        /* end of jpa */

        // jpa 방식 또는 kafka 둘중 하나만 활성화해서 사용해야 한다.
        // 만약 분산추적을 할 생각이라면 kafka 내용을 전체 주석처리한뒤 jpa 부분을 활성화해주세요.
        /* kafka */
        //orderDto.setOrderId(UUID.randomUUID().toString());
        //orderDto.setTotalPrice(orderDto.getUnitPrice() * orderDto.getQty());
        // send this order to the kafka
        // kafkaProducer.send("example-catalog-topic", orderDto); // 카탈로그 서비스의 KafkaConsumer 에 전달할 topic 명을 작성해야 한다.
        // orderProducer.send("example-catalog-topic", orderDto); // topic 이름: orders. sink-connect 를 생성할때 토픽명을 orders 로 주어야한다.
        /* end of kafka */

        ResponseOrder responseOrder = new ResponseOrder();
        BeanUtils.copyProperties(orderDto, responseOrder); // jpa 방식을 사용한다면 orderDto 가 아닌 order 를 복사해 넣어야 함.
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrdersByUserId(@PathVariable("userId") String userId) {
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
