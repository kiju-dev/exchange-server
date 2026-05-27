package com.exchange.exchange_server.market.controller.response;

import java.time.LocalDateTime;

public record MarketOpenResponse(
        ExchangeStatus exchangeStatus,
        LocalDateTime openedAt
) {
}