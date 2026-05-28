package com.exchange.exchange_server.order.controller.response;

import java.util.List;

public record PriceLevel(
        long totalQuantity,
        List<OrderSummary> orders
) {
}
