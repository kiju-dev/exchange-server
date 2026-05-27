package com.exchange.exchange_server.market.controller;

import com.exchange.exchange_server.market.controller.response.MarketCloseResponse;
import com.exchange.exchange_server.market.controller.response.MarketOpenResponse;
import com.exchange.exchange_server.market.service.MarketService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static com.exchange.exchange_server.market.controller.response.ExchangeStatus.RUNNING;
import static com.exchange.exchange_server.market.controller.response.ExchangeStatus.STOPPED;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MarketController.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MarketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MarketService marketService;

    @Test
    void 시장을_연다() throws Exception {
        // given
        MarketOpenResponse response = new MarketOpenResponse(RUNNING, LocalDateTime.now());

        given(marketService.openMarket()).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/market/open"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exchangeStatus").value("RUNNING"))
                .andExpect(jsonPath("$.openedAt").exists());

        verify(marketService).openMarket();
    }

    @Test
    void 시장을_닫는다() throws Exception {
        // given
        MarketCloseResponse response = new MarketCloseResponse(STOPPED, LocalDateTime.now());

        given(marketService.closeMarket()).willReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/market/close"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exchangeStatus").value("STOPPED"))
                .andExpect(jsonPath("$.closedAt").exists());

        verify(marketService).closeMarket();
    }
}