package com.exchange.exchange_server.order.controller.request;

import com.exchange.exchange_server.order.OrderSide;

public record CancelRequest(
        Long orderId,
        Long stockId,
        OrderSide side,
        long price
) {
}
