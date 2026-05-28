package com.exchange.exchange_server.order.controller.response;

import java.util.Map;

public record OrderBookResponse(
        Map<Long, PriceLevel> buy,
        Map<Long, PriceLevel> sell
) {
}
