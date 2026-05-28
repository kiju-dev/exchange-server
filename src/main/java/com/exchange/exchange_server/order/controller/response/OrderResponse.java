package com.exchange.exchange_server.order.controller.response;

import java.util.List;

public record OrderResponse(
        MatchResult matchResult,
        Long takerOrderId,
        List<MatchedMakerOrder> makers,
        long price,
        long totalMatchedQuantity
) {
}
