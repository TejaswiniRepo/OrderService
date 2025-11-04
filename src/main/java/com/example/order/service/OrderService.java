package com.example.order.service;

import com.example.order.api.CreateOrderRequest;
import com.example.order.domain.IdempotencyKey;
import com.example.order.domain.Order;
import com.example.order.domain.OrderItem;
import com.example.order.domain.OrderStatus;
import com.example.order.domain.PaymentStatus;
import com.example.order.repository.IdempotencyRepository;
import com.example.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final IdempotencyRepository idempotencyRepository;
    private final MockExternalServiceClient mockClient;

    public OrderService(OrderRepository orderRepository,
                        IdempotencyRepository idempotencyRepository,
                        MockExternalServiceClient mockClient) {
        this.orderRepository = orderRepository;
        this.idempotencyRepository = idempotencyRepository;
        this.mockClient = mockClient;
    }

    public synchronized Order createOrder(String idempotencyKey, CreateOrderRequest req, String correlationId) throws IllegalArgumentException {
        // Idempotency handling
        Optional<IdempotencyKey> existing = idempotencyRepository.findById(idempotencyKey);
        if (existing.isPresent()) {
            IdempotencyKey k = existing.get();
            if ("COMPLETED".equals(k.getStatus()) && k.getOrderId() != null) {
                return orderRepository.findById(k.getOrderId()).orElse(null);
            } else {
                throw new IllegalArgumentException("Request with same idempotency key is in progress or failed");
            }
        }
        IdempotencyKey key = new IdempotencyKey();
        key.setId(idempotencyKey);
        key.setResource("create-order");
        key.setStatus("IN_PROGRESS");
        key.setCreatedAt(Instant.now());
        idempotencyRepository.save(key);

        // Basic validations
        if (req.items == null || req.items.isEmpty()) throw new IllegalArgumentException("items required");
        if (req.items.size() > 20) throw new IllegalArgumentException("max 20 distinct items allowed");
        for (CreateOrderRequest.Item it : req.items) {
            if (it.quantity == null || it.quantity < 1 || it.quantity > 5) throw new IllegalArgumentException("each item qty must be 1..5");
        }

        // Fetch menu item details from mock external service and validate availability & price
        Map<Long, MockExternalServiceClient.MenuItem> menuMap = mockClient.getMenuForRestaurant(req.restaurantId);
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CreateOrderRequest.Item it : req.items) {
            MockExternalServiceClient.MenuItem mi = menuMap.get(it.itemId);
            if (mi == null) {
                key.setStatus("FAILED");
                idempotencyRepository.save(key);
                throw new IllegalArgumentException("item " + it.itemId + " not found in restaurant " + req.restaurantId);
            }
            if (!mi.isAvailable) {
                key.setStatus("FAILED");
                idempotencyRepository.save(key);
                throw new IllegalArgumentException("item " + it.itemId + " not available");
            }
            BigDecimal line = mi.price.multiply(BigDecimal.valueOf(it.quantity));
            OrderItem oi = new OrderItem();
            oi.setItemId(it.itemId);
            oi.setName(mi.name);
            oi.setQuantity(it.quantity);
            oi.setUnitPrice(mi.price);
            oi.setLinePrice(line);
            orderItems.add(oi);
            subtotal = subtotal.add(line);
        }

        BigDecimal tax = subtotal.multiply(BigDecimal.valueOf(0.05)).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal deliveryFee = mockClient.getDeliveryFeeForAddress(req.addressId);
        BigDecimal serverTotal = subtotal.add(tax).add(deliveryFee).setScale(2, BigDecimal.ROUND_HALF_UP);

        if (req.clientTotal == null) {
            key.setStatus("FAILED");
            idempotencyRepository.save(key);
            throw new IllegalArgumentException("clientTotal required");
        }
        if (serverTotal.compareTo(req.clientTotal) != 0) {
            key.setStatus("FAILED");
            idempotencyRepository.save(key);
            throw new IllegalArgumentException("total mismatch: serverTotal=" + serverTotal + " clientTotal=" + req.clientTotal);
        }

        Order order = new Order();
        order.setCustomerId(req.customerId);
        order.setRestaurantId(req.restaurantId);
        order.setAddressId(req.addressId);
        order.setItems(orderItems);
        order.setSubtotal(subtotal);
        order.setTax(tax);
        order.setDeliveryFee(deliveryFee);
        order.setOrderTotal(serverTotal);
        order.setOrderStatus(OrderStatus.CREATED);
        order.setPaymentStatus("COD".equalsIgnoreCase(req.paymentMethod) ? PaymentStatus.PENDING : PaymentStatus.PENDING);
        order.setCreatedAt(Instant.now());
        order.setUpdatedAt(Instant.now());
        order.setCorrelationId(correlationId == null ? UUID.randomUUID().toString() : correlationId);
        Map<String,String> proj = new HashMap<>();
        proj.put("restaurantName", mockClient.getRestaurantName(req.restaurantId));
        proj.put("addressCity", mockClient.getAddressCity(req.addressId));
        order.setProjections(proj);

        Order saved = orderRepository.save(order);

        // If non-COD -> call mock payment
        if (!"COD".equalsIgnoreCase(req.paymentMethod)) {
            boolean paid = mockClient.chargePayment(saved.getId(), saved.getOrderTotal());
            if (paid) {
                saved.setOrderStatus(OrderStatus.CONFIRMED);
                saved.setPaymentStatus(PaymentStatus.SUCCESS);
                orderRepository.save(saved);
                mockClient.assignDriver(saved.getId(), saved.getRestaurantId(), saved.getAddressId());
            } else {
                saved.setPaymentStatus(PaymentStatus.FAILED);
                orderRepository.save(saved);
            }
        } else {
            // For COD, set PENDING and assign driver
            mockClient.assignDriver(saved.getId(), saved.getRestaurantId(), saved.getAddressId());
        }

        key.setStatus("COMPLETED");
        key.setOrderId(saved.getId());
        idempotencyRepository.save(key);
        return saved;
    }

    public Optional<Order> getById(String id) {
        return orderRepository.findById(id);
    }

    public List<Order> listByCustomer(Long customerId) {
        if (customerId == null) return orderRepository.findAll();
        return orderRepository.findByCustomerId(customerId);
    }
}
