package com.exchange.exchange_server.order.controller.response;

import java.time.LocalDateTime;

public record OrderSummary(
        Long orderId,
        long unfilledQuantity,
        LocalDateTime createdAt
) {
}