package com.exchange.exchange_server.order.controller.request;

import com.exchange.exchange_server.order.OrderSide;

import java.time.LocalDateTime;

public record OrderRequest(
        Long orderId,
        Long stockId,
        long price,
        long quantity,
        OrderSide side,
        LocalDateTime createdAt
) {
}
