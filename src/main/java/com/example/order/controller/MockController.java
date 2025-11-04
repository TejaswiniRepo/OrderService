package com.example.order.controller;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/mocks")
public class MockController {

    @GetMapping("/restaurants/{id}")
    public Map<String,Object> restaurant(@PathVariable Long id) {
        return Map.of("id", id, "name", "Spice Hub "+id, "cuisine", "Indian", "city", "Hyderabad", "is_open", true, "rating", 4.2);
    }

    @GetMapping("/restaurants/{rid}/items/{iid}")
    public Map<String,Object> menuItem(@PathVariable Long rid, @PathVariable Long iid) {
        if (iid == 314L) {
            return Map.of("itemId", iid, "name", "Veg Biryani", "price", 131.82, "isAvailable", false);
        }
        return Map.of("itemId", iid, "name", "Sample Item "+iid, "price", iid==317L?129.52:97.65, "isAvailable", true);
    }

    @GetMapping("/customers/{id}")
    public Map<String,Object> customer(@PathVariable Long id) {
        return Map.of("customerId", id, "name", "Customer "+id, "email", "cust"+id+"@example.com");
    }

    @PostMapping("/payments/charge")
    public Map<String,Object> paymentCharge(@RequestBody Map<String,Object> body) {
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        boolean ok = amount.compareTo(new BigDecimal("2000")) < 0;
        return Map.of("status", ok?"SUCCESS":"FAILED", "reference", "mockref-"+System.currentTimeMillis());
    }

    @PostMapping("/delivery/assign")
    public Map<String,Object> assign(@RequestBody Map<String,Object> body) {
        return Map.of("status","ASSIGNED","driverId", 101, "assignedAt", System.currentTimeMillis());
    }
}
