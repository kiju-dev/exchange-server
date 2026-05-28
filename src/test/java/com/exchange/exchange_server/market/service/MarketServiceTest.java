package com.exchange.exchange_server.market.service;

import com.exchange.exchange_server.market.ExchangeState;
import com.exchange.exchange_server.market.controller.response.MarketCloseResponse;
import com.exchange.exchange_server.market.controller.response.MarketOpenResponse;
import com.exchange.exchange_server.order.orderbook.OrderBookStore;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.exchange.exchange_server.market.controller.response.ExchangeStatus.RUNNING;
import static com.exchange.exchange_server.market.controller.response.ExchangeStatus.STOPPED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MarketServiceTest {

    @Mock
    private ExchangeState exchangeState;

    @Mock
    private OrderBookStore orderBookStore;

    @InjectMocks
    private MarketService marketService;

    @Test
    void 시장을_연다() {
        // given
        given(exchangeState.getStatus()).willReturn(RUNNING);

        // when
        MarketOpenResponse response = marketService.openMarket();

        // then
        assertThat(response.exchangeStatus()).isEqualTo(RUNNING);
        assertThat(response.openedAt()).isNotNull();
        verify(exchangeState).open();
        verify(exchangeState).getStatus();
    }

    @Test
    void 시장을_닫는다() {
        // given
        given(exchangeState.getStatus()).willReturn(STOPPED);

        // when
        MarketCloseResponse response = marketService.closeMarket();

        // then
        assertThat(response.exchangeStatus()).isEqualTo(STOPPED);
        assertThat(response.closedAt()).isNotNull();
        verify(exchangeState).close();
        verify(exchangeState).getStatus();
    }

}