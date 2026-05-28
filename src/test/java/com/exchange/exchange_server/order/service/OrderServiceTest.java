package com.exchange.exchange_server.order.service;

import com.exchange.exchange_server.market.ExchangeState;
import com.exchange.exchange_server.order.Order;
import com.exchange.exchange_server.order.controller.request.CancelRequest;
import com.exchange.exchange_server.order.controller.request.OrderRequest;
import com.exchange.exchange_server.order.controller.response.OrderResponse;
import com.exchange.exchange_server.order.orderbook.OrderBook;
import com.exchange.exchange_server.order.orderbook.OrderBookStore;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static com.exchange.exchange_server.order.OrderSide.BUY;
import static com.exchange.exchange_server.order.controller.response.MatchResult.CANCELLED;
import static com.exchange.exchange_server.order.controller.response.MatchResult.UNMATCHED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderServiceTest {

    @Mock
    private ExchangeState exchangeState;

    @Mock
    private OrderBookStore orderBookStore;

    @Mock
    private OrderBook orderBook;

    @InjectMocks
    private OrderService orderService;

    @Test
    void 장_상태를_검증한_후_orderBook에_주문을_전달한다() {
        // given
        OrderRequest request = new OrderRequest(
                1L,
                10L,
                1000L,
                5L,
                BUY,
                LocalDateTime.now()
        );

        OrderResponse expectedResponse =
                new OrderResponse(UNMATCHED, null, List.of(), 0L, 0L);

        given(orderBookStore.getOrderBook(request.stockId()))
                .willReturn(orderBook);

        given(orderBook.place(org.mockito.ArgumentMatchers.any(Order.class)))
                .willReturn(expectedResponse);

        // when
        OrderResponse response = orderService.placeOrder(request);

        // then
        assertThat(response).isEqualTo(expectedResponse);

        verify(exchangeState).validateRunning();
        verify(orderBookStore).getOrderBook(10L);

        ArgumentCaptor<Order> orderCaptor =
                ArgumentCaptor.forClass(Order.class);

        verify(orderBook).place(orderCaptor.capture());

        Order capturedOrder = orderCaptor.getValue();

        assertThat(capturedOrder.getOrderId()).isEqualTo(1L);
        assertThat(capturedOrder.getStockId()).isEqualTo(10L);
        assertThat(capturedOrder.getPrice()).isEqualTo(1000L);
        assertThat(capturedOrder.getQuantity()).isEqualTo(5L);
        assertThat(capturedOrder.getRemainingQuantity()).isEqualTo(5L);
        assertThat(capturedOrder.getSide()).isEqualTo(BUY);
    }

    @Test
    void 주문_취소_요청_시_장_상태를_검증하고_orderbook에_취소를_위임한다() {
        // given
        CancelRequest request = new CancelRequest(10L, 1L, BUY, 10000L);

        OrderResponse expectedResponse =
                new OrderResponse(CANCELLED, null, List.of(), 0L, 0L);

        given(orderBookStore.getOrderBook(request.stockId()))
                .willReturn(orderBook);

        given(orderBook.cancel(request.orderId()))
                .willReturn(expectedResponse);

        // when
        OrderResponse response = orderService.cancelOrder(request);

        // then
        assertThat(response).isEqualTo(expectedResponse);

        verify(exchangeState).validateRunning();
        verify(orderBookStore).getOrderBook(1L);
        verify(orderBook).cancel(10L);
    }
}