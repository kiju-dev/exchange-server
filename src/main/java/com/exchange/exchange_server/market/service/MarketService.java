package com.exchange.exchange_server.market.service;

import com.exchange.exchange_server.market.ExchangeState;
import com.exchange.exchange_server.market.controller.response.MarketCloseResponse;
import com.exchange.exchange_server.market.controller.response.MarketOpenResponse;
import com.exchange.exchange_server.order.orderbook.OrderBookStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MarketService {

    private final ExchangeState exchangeState;
    private final OrderBookStore orderBookStore;

    public MarketOpenResponse openMarket() {
        exchangeState.open();
        return new MarketOpenResponse(exchangeState.getStatus(), LocalDateTime.now());
    }

    public MarketCloseResponse closeMarket() {
        orderBookStore.closeAll();
        exchangeState.close();
        return new MarketCloseResponse(exchangeState.getStatus(), LocalDateTime.now());
    }
}
