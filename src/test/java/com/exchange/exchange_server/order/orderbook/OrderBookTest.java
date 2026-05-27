package com.exchange.exchange_server.order.orderbook;

import com.exchange.exchange_server.order.Order;
import com.exchange.exchange_server.order.OrderSide;
import com.exchange.exchange_server.order.controller.response.OrderResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.exchange.exchange_server.order.OrderSide.BUY;
import static com.exchange.exchange_server.order.OrderSide.SELL;
import static com.exchange.exchange_server.order.controller.response.MatchResult.MATCHED;
import static com.exchange.exchange_server.order.controller.response.MatchResult.UNMATCHED;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderBookTest {

    @Test
    void 반대편_주문이_없으면_UNMATCHED를_반환한다() {
        // given
        OrderBook orderBook = new OrderBook();
        Order buyOrder = createOrder(1L, BUY, 1000L, 10L);

        // when
        OrderResponse response = orderBook.place(buyOrder);

        // then
        assertThat(response.matchResult()).isEqualTo(UNMATCHED);
        assertThat(response.takerOrderId()).isNull();
        assertThat(response.makers()).isEmpty();
        assertThat(response.price()).isZero();
        assertThat(response.totalMatchedQuantity()).isZero();
    }

    @Test
    void 같은_가격의_반대편_주문이_있으면_MATCHED를_반환한다() {
        // given
        OrderBook orderBook = new OrderBook();
        Order sellOrder = createOrder(1L, SELL, 1000L, 10L);
        Order buyOrder = createOrder(2L, BUY, 1000L, 10L);
        orderBook.place(sellOrder);

        // when
        OrderResponse response = orderBook.place(buyOrder);

        // then
        assertThat(response.matchResult()).isEqualTo(MATCHED);
        assertThat(response.takerOrderId()).isEqualTo(2L);
        assertThat(response.price()).isEqualTo(1000L);
        assertThat(response.totalMatchedQuantity()).isEqualTo(10L);
        assertThat(response.makers()).hasSize(1);
        assertThat(response.makers().get(0).orderId()).isEqualTo(1L);
        assertThat(response.makers().get(0).matchedQuantity()).isEqualTo(10L);
    }

    @Test
    void 가격이_다르면_체결되지_않고_UNMATCHED를_반환한다() {
        // given
        OrderBook orderBook = new OrderBook();
        Order sellOrder = createOrder(1L, SELL, 1000L, 10L);
        Order buyOrder = createOrder(2L, BUY, 1100L, 10L);
        orderBook.place(sellOrder);

        // when
        OrderResponse response = orderBook.place(buyOrder);

        // then
        assertThat(response.matchResult()).isEqualTo(UNMATCHED);
        assertThat(response.makers()).isEmpty();
        assertThat(response.totalMatchedQuantity()).isZero();
    }

    @Test
    void 같은_가격이면_먼저_들어온_주문부터_FIFO로_체결된다() {
        // given
        OrderBook orderBook = new OrderBook();
        Order firstSellOrder = createOrder(1L, SELL, 1000L, 5L);
        Order secondSellOrder = createOrder(2L, SELL, 1000L, 5L);
        Order buyOrder = createOrder(3L, BUY, 1000L, 10L);
        orderBook.place(firstSellOrder);
        orderBook.place(secondSellOrder);

        // when
        OrderResponse response = orderBook.place(buyOrder);

        // then
        assertThat(response.matchResult()).isEqualTo(MATCHED);
        assertThat(response.totalMatchedQuantity()).isEqualTo(10L);
        assertThat(response.makers()).hasSize(2);
        assertThat(response.makers().get(0).orderId()).isEqualTo(1L);
        assertThat(response.makers().get(0).matchedQuantity()).isEqualTo(5L);
        assertThat(response.makers().get(1).orderId()).isEqualTo(2L);
        assertThat(response.makers().get(1).matchedQuantity()).isEqualTo(5L);
    }

    @Test

    void 일부만_체결되면_남은_수량은_주문장에_저장된다() {
        // given
        OrderBook orderBook = new OrderBook();
        Order sellOrder = createOrder(1L, SELL, 1000L, 5L);
        Order buyOrder = createOrder(2L, BUY, 1000L, 10L);
        Order nextSellOrder = createOrder(3L, SELL, 1000L, 5L);
        orderBook.place(sellOrder);

        // when
        OrderResponse firstResponse = orderBook.place(buyOrder);
        OrderResponse secondResponse = orderBook.place(nextSellOrder);

        // then
        assertThat(firstResponse.matchResult()).isEqualTo(MATCHED);
        assertThat(firstResponse.totalMatchedQuantity()).isEqualTo(5L);
        assertThat(secondResponse.matchResult()).isEqualTo(MATCHED);
        assertThat(secondResponse.takerOrderId()).isEqualTo(3L);
        assertThat(secondResponse.totalMatchedQuantity()).isEqualTo(5L);
        assertThat(secondResponse.makers()).hasSize(1);
        assertThat(secondResponse.makers().get(0).orderId()).isEqualTo(2L);
        assertThat(secondResponse.makers().get(0).matchedQuantity()).isEqualTo(5L);
    }

    @Test
    void close를_호출하면_주문장이_초기화된다() {
        // given
        OrderBook orderBook = new OrderBook();
        Order sellOrder = createOrder(1L, SELL, 1000L, 10L);
        Order buyOrder = createOrder(2L, BUY, 1000L, 10L);
        orderBook.place(sellOrder);

        // when
        orderBook.close();
        OrderResponse response = orderBook.place(buyOrder);

        // then
        assertThat(response.matchResult()).isEqualTo(UNMATCHED);
        assertThat(response.makers()).isEmpty();
        assertThat(response.totalMatchedQuantity()).isZero();
    }

    private Order createOrder(
            Long orderId,
            OrderSide side,
            long price,
            long quantity
    ) {
        return new Order(
                orderId,
                1L,
                price,
                quantity,
                side,
                LocalDateTime.now()
        );
    }
}