package com.example.order.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class MockExternalServiceClient {

    public static class MenuItem {
        public Long itemId;
        public String name;
        public BigDecimal price;
        public boolean isAvailable;
        public MenuItem(Long itemId, String name, BigDecimal price, boolean isAvailable) {
            this.itemId = itemId; this.name = name; this.price = price; this.isAvailable = isAvailable;
        }
    }

    // In-memory mock menus for demo
    public Map<Long, MenuItem> getMenuForRestaurant(Long restaurantId) {
        Map<Long, MenuItem> m = new HashMap<>();
        // provide some sample items
        m.put(317L, new MenuItem(317L, "Paneer Butter Masala", new BigDecimal("129.52"), true));
        m.put(316L, new MenuItem(316L, "Garlic Naan", new BigDecimal("97.65"), true));
        m.put(314L, new MenuItem(314L, "Veg Biryani", new BigDecimal("131.82"), false)); // unavailable
        return m;
    }

    public String getRestaurantName(Long restaurantId) {
        return "Spice Hub #" + restaurantId;
    }

    public String getAddressCity(Long addressId) {
        return "Hyderabad";
    }

    public BigDecimal getDeliveryFeeForAddress(Long addressId) {
        return new BigDecimal("30.00");
    }

    public boolean chargePayment(String orderId, BigDecimal amount) {
        // mock payment success for demo except when amount is > 2000
        return amount.compareTo(new BigDecimal("2000")) < 0;
    }

    public boolean assignDriver(String orderId, Long restaurantId, Long addressId) {
        // pretend driver assigned
        return true;
    }
}
