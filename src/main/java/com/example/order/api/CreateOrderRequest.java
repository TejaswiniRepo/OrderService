package com.example.order.api;

import java.math.BigDecimal;
import java.util.List;

public class CreateOrderRequest {
    public Long customerId;
    public Long restaurantId;
    public Long addressId;
    public List<Item> items;
    public BigDecimal clientTotal;
    public String paymentMethod;

    public static class Item {
        public Long itemId;
        public Integer quantity;
    }
}
