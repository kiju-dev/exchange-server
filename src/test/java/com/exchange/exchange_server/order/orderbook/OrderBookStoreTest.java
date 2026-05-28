package com.exchange.exchange_server.order.orderbook;

import com.exchange.exchange_server.order.Order;
import com.exchange.exchange_server.order.controller.response.OrderResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.exchange.exchange_server.order.OrderSide.BUY;
import static com.exchange.exchange_server.order.OrderSide.SELL;
import static com.exchange.exchange_server.order.controller.response.MatchResult.UNMATCHED;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderBookStoreTest {

    @Test
    void 같은_stockId로_조회하면_같은_OrderBook을_반환한다() {
        // given
        OrderBookStore orderBookStore = new OrderBookStore();

        // when
        OrderBook first = orderBookStore.getOrderBook(1L);
        OrderBook second = orderBookStore.getOrderBook(1L);

        // then
        assertThat(first).isSameAs(second);
    }

    @Test
    void 다른_stockId로_조회하면_다른_OrderBook을_반환한다() {
        // given
        OrderBookStore orderBookStore = new OrderBookStore();

        // when
        OrderBook first = orderBookStore.getOrderBook(1L);
        OrderBook second = orderBookStore.getOrderBook(2L);

        // then
        assertThat(first).isNotSameAs(second);
    }

    @Test
    void closeAll을_호출하면_모든_주문장이_초기화된다() {
        // given
        OrderBookStore orderBookStore = new OrderBookStore();

        OrderBook firstOrderBook = orderBookStore.getOrderBook(1L);
        OrderBook secondOrderBook = orderBookStore.getOrderBook(2L);

        firstOrderBook.place(createOrder(1L, 1L, SELL, 1000L, 10L));
        secondOrderBook.place(createOrder(2L, 2L, SELL, 2000L, 10L));

        // when
        orderBookStore.closeAll();

        // then
        OrderResponse firstResponse =
                firstOrderBook.place(createOrder(3L, 1L, BUY, 1000L, 10L));

        OrderResponse secondResponse =
                secondOrderBook.place(createOrder(4L, 2L, BUY, 2000L, 10L));

        assertThat(firstResponse.matchResult()).isEqualTo(UNMATCHED);
        assertThat(secondResponse.matchResult()).isEqualTo(UNMATCHED);
    }

    private Order createOrder(
            Long orderId,
            Long stockId,
            com.exchange.exchange_server.order.OrderSide side,
            long price,
            long quantity
    ) {
        return new Order(
                orderId,
                stockId,
                price,
                quantity,
                side,
                LocalDateTime.now()
        );
    }
}