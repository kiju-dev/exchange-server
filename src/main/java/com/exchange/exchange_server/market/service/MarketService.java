package com.exchange.exchange_server.market.service;

import com.exchange.exchange_server.market.ExchangeState;
import com.exchange.exchange_server.market.controller.response.MarketOpenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MarketService {

    private final ExchangeState exchangeState;

    public MarketOpenResponse openMarket() {
        exchangeState.open();
        return new MarketOpenResponse(exchangeState.getStatus(), LocalDateTime.now());
    }
}
