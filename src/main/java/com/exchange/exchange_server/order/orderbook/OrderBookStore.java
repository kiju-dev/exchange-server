package com.exchange.exchange_server.order.orderbook;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderBookStore {

    private final Map<Long, OrderBook> orderBooks = new HashMap<>();

    public OrderBook getOrderBook(Long stockId) {
        OrderBook orderBook = orderBooks.get(stockId);
        if (orderBook == null) {
            orderBook = new OrderBook();
            orderBooks.put(stockId, orderBook);
        }
        return orderBook;
    }

    public void closeAll() {
        for (OrderBook orderBook : orderBooks.values()) {
            orderBook.close();
        }
    }
}
