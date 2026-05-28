package com.exchange.exchange_server.order;

import com.exchange.exchange_server.order.controller.request.OrderRequest;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Order {

    private Long orderId;
    private Long stockId;
    private long price;
    private long quantity;
    private OrderSide side;
    private LocalDateTime createdAt;
    private long remainingQuantity;

    public Order(
            Long orderId,
            Long stockId,
            long price,
            long quantity,
            OrderSide side,
            LocalDateTime createdAt
    ) {
        this.orderId = orderId;
        this.stockId = stockId;
        this.price = price;
        this.quantity = quantity;
        this.side = side;
        this.createdAt = createdAt;
        this.remainingQuantity = quantity;
    }

    public static Order from(OrderRequest request) {
        return new Order(
                request.orderId(),
                request.stockId(),
                request.price(),
                request.quantity(),
                request.side(),
                request.createdAt()
        );
    }

    public void decreaseQuantity(long quantity) {
        this.remainingQuantity -= quantity;
    }
}
