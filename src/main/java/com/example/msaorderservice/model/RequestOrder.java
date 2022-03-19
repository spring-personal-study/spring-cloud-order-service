package com.example.msaorderservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RequestOrder {

    private String productId;
    private Integer qty;
    private Integer unitPrice;
}
