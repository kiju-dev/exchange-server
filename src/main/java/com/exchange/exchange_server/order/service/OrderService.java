package com.exchange.exchange_server.order.service;

import com.exchange.exchange_server.market.ExchangeState;
import com.exchange.exchange_server.order.Order;
import com.exchange.exchange_server.order.controller.request.CancelRequest;
import com.exchange.exchange_server.order.controller.request.OrderRequest;
import com.exchange.exchange_server.order.controller.response.OrderBookResponse;
import com.exchange.exchange_server.order.controller.response.OrderResponse;
import com.exchange.exchange_server.order.orderbook.OrderBook;
import com.exchange.exchange_server.order.orderbook.OrderBookStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ExchangeState exchangeState;
    private final OrderBookStore orderBookStore;

    public OrderResponse placeOrder(OrderRequest request) {
        exchangeState.validateRunning();

        OrderBook orderBook = orderBookStore.getOrderBook(request.stockId());
        return orderBook.place(Order.from(request));
    }

    public OrderResponse cancelOrder(CancelRequest request) {
        exchangeState.validateRunning();

        OrderBook orderBook = orderBookStore.getOrderBook(request.stockId());
        return orderBook.cancel(request.orderId());
    }

    public OrderBookResponse getOrderBook(Long stockId) {
        exchangeState.validateRunning();

        OrderBook orderBook = orderBookStore.getOrderBook(stockId);
        return orderBook.getOrderBook();
    }
}
