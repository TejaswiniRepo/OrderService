package com.example.order.config;

import com.example.order.domain.Order;
import com.example.order.domain.OrderItem;
import com.example.order.domain.OrderStatus;
import com.example.order.domain.PaymentStatus;
import com.example.order.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner init(OrderRepository orderRepository) {
        return args -> {
            if (orderRepository.count() == 0) {
                Order o = new Order();
                o.setCustomerId(54L);
                o.setRestaurantId(36L);
                o.setAddressId(73L);
                OrderItem i1 = new OrderItem();
                i1.setItemId(317L);
                i1.setName("Paneer Butter Masala");
                i1.setQuantity(3);
                i1.setUnitPrice(new BigDecimal("129.52"));
                i1.setLinePrice(new BigDecimal("388.56"));
                o.setItems(List.of(i1));
                o.setSubtotal(new BigDecimal("388.56"));
                o.setTax(new BigDecimal("19.43"));
                o.setDeliveryFee(new BigDecimal("30.00"));
                o.setOrderTotal(new BigDecimal("438.0"));
                o.setOrderStatus(OrderStatus.DELIVERED);
                o.setPaymentStatus(PaymentStatus.SUCCESS);
                o.setCreatedAt(Instant.now());
                o.setUpdatedAt(Instant.now());
                o.setProjections(Map.of("restaurantName","Spice Hub", "addressCity","Hyderabad"));
                orderRepository.save(o);
            }
        };
    }
}
