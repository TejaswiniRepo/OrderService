package com.example.order.domain;

import java.math.BigDecimal;

public class OrderItem {
    private Long itemId;
    private String name;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal linePrice;

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getLinePrice() { return linePrice; }
    public void setLinePrice(BigDecimal linePrice) { this.linePrice = linePrice; }
}
