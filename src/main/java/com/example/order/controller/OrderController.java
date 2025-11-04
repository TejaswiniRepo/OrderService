package com.example.order.controller;

import com.example.order.api.CreateOrderRequest;
import com.example.order.api.OrderResponse;
import com.example.order.domain.Order;
import com.example.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestHeader(value = "Idempotency-Key", required = true) String idempotencyKey,
                                         @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId,
                                         @RequestBody CreateOrderRequest request) {
        try {
            Order o = orderService.createOrder(idempotencyKey, request, correlationId);
            return ResponseEntity.status(HttpStatus.CREATED).body(new OrderResponse(o));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("code","ORDER_VALIDATION_ERROR","message",e.getMessage(),"correlationId", correlationId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("code","INTERNAL_ERROR","message",e.getMessage(),"correlationId", correlationId));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        Optional<Order> o = orderService.getById(id);
        return o.map(ord -> ResponseEntity.ok(new OrderResponse(ord)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("code","NOT_FOUND","message","order not found")));
    }

    @GetMapping
    public ResponseEntity<List<com.example.order.domain.Order>> list(@RequestParam(value = "customerId", required = false) Long customerId) {
        return ResponseEntity.ok(orderService.listByCustomer(customerId));
    }
}
