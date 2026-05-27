package com.exchange.exchange_server.market.controller.response;

import java.time.LocalDateTime;

public record MarketCloseResponse(
        ExchangeStatus exchangeStatus,
        LocalDateTime closedAt
) {
}
