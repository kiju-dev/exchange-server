package com.exchange.exchange_server.order.controller.response;

public record MatchedMakerOrder(
        Long orderId,
        long matchedQuantity
) {
}
