package com.example.order.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private Long customerId;
    private Long restaurantId;
    private Long addressId;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private List<OrderItem> items;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal deliveryFee;
    private BigDecimal orderTotal;
    private Map<String, String> projections;
    private Instant createdAt;
    private Instant updatedAt;
    private String correlationId;

    // getters and setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    public Long getAddressId() { return addressId; }
    public void setAddressId(Long addressId) { this.addressId = addressId; }
    public OrderStatus getOrderStatus() { return orderStatus; }
    public void setOrderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public java.math.BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(java.math.BigDecimal subtotal) { this.subtotal = subtotal; }
    public java.math.BigDecimal getTax() { return tax; }
    public void setTax(java.math.BigDecimal tax) { this.tax = tax; }
    public java.math.BigDecimal getDeliveryFee() { return deliveryFee; }
    public void setDeliveryFee(java.math.BigDecimal deliveryFee) { this.deliveryFee = deliveryFee; }
    public java.math.BigDecimal getOrderTotal() { return orderTotal; }
    public void setOrderTotal(java.math.BigDecimal orderTotal) { this.orderTotal = orderTotal; }
    public java.util.Map<String, String> getProjections() { return projections; }
    public void setProjections(java.util.Map<String, String> projections) { this.projections = projections; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
}
