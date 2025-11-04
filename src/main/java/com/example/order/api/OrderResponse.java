package com.example.order.api;

import com.example.order.domain.Order;
public class OrderResponse {
    public Order order;
    public OrderResponse(Order order) { this.order = order; }
}
