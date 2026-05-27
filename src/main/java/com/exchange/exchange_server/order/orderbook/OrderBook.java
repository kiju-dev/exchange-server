package com.exchange.exchange_server.order.orderbook;

import com.exchange.exchange_server.order.Order;
import com.exchange.exchange_server.order.controller.response.MatchedMakerOrder;
import com.exchange.exchange_server.order.controller.response.OrderResponse;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.exchange.exchange_server.order.OrderSide.BUY;
import static com.exchange.exchange_server.order.controller.response.MatchResult.MATCHED;
import static com.exchange.exchange_server.order.controller.response.MatchResult.UNMATCHED;

public class OrderBook {

    private final Map<Long, Deque<Order>> buyBook = new HashMap<>();
    private final Map<Long, Deque<Order>> sellBook = new HashMap<>();
    private final Map<Long, Order> orderMap = new HashMap<>();

    public OrderResponse place(Order order) {
        if (order.getSide() == BUY) {
            return match(order, sellBook);
        }
        return match(order, buyBook);
    }

    private OrderResponse match(Order takerOrder, Map<Long, Deque<Order>> makerOrderBook) {
        Deque<Order> makerOrders = makerOrderBook.get(takerOrder.getPrice());

        if (makerOrders == null || makerOrders.isEmpty()) {
            addToOrderBook(takerOrder);
            return new OrderResponse(UNMATCHED, null, List.of(), 0L, 0L);
        }

        List<MatchedMakerOrder> makers = new ArrayList<>();
        long totalMatchedQuantity = 0L;

        while (takerOrder.getRemainingQuantity() > 0 && !makerOrders.isEmpty()) {
            Order makerOrder = makerOrders.getFirst();

            long matchedQuantity = Math.min(takerOrder.getRemainingQuantity(), makerOrder.getRemainingQuantity());
            takerOrder.decreaseQuantity(matchedQuantity);
            makerOrder.decreaseQuantity(matchedQuantity);

            makers.add(new MatchedMakerOrder(makerOrder.getOrderId(), matchedQuantity));
            totalMatchedQuantity += matchedQuantity;

            if (makerOrder.getRemainingQuantity() == 0) {
                makerOrders.pollFirst();
                orderMap.remove(makerOrder.getOrderId());
            }
        }

        if (makerOrders.isEmpty()) {
            makerOrderBook.remove(takerOrder.getPrice());
        }
        if (takerOrder.getRemainingQuantity() > 0) {
            addToOrderBook(takerOrder);
        }

        return new OrderResponse(
                MATCHED,
                takerOrder.getOrderId(),
                makers,
                takerOrder.getPrice(),
                totalMatchedQuantity
        );
    }

    private void addToOrderBook(Order order) {
        Map<Long, Deque<Order>> orderBook;
        if (order.getSide() == BUY) {
            orderBook = buyBook;
        } else {
            orderBook = sellBook;
        }

        Deque<Order> orders = orderBook.get(order.getPrice());
        if (orders == null) {
            orders = new ArrayDeque<>();
            orderBook.put(order.getPrice(), orders);
        }

        orders.addLast(order);
        orderMap.put(order.getOrderId(), order);
    }

    public void close() {
        buyBook.clear();
        sellBook.clear();
        orderMap.clear();
    }
}
