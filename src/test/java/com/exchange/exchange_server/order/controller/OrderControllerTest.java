package com.exchange.exchange_server.order.controller;

import com.exchange.exchange_server.order.controller.request.CancelRequest;
import com.exchange.exchange_server.order.controller.request.OrderRequest;
import com.exchange.exchange_server.order.controller.response.MatchedMakerOrder;
import com.exchange.exchange_server.order.controller.response.OrderResponse;
import com.exchange.exchange_server.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static com.exchange.exchange_server.order.OrderSide.BUY;
import static com.exchange.exchange_server.order.controller.response.MatchResult.CANCELLED;
import static com.exchange.exchange_server.order.controller.response.MatchResult.MATCHED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @Test
    void 주문을_접수하고_체결_결과를_반환한다() throws Exception {
        // given
        OrderRequest request = new OrderRequest(
                1L,
                1L,
                1000L,
                10L,
                BUY,
                LocalDateTime.now()
        );

        OrderResponse response = new OrderResponse(
                MATCHED,
                1L,
                List.of(new MatchedMakerOrder(2L, 10L)),
                1000L,
                10L
        );

        given(orderService.placeOrder(any(OrderRequest.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(post("/api/v1/market/order")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matchResult").value("MATCHED"))
                .andExpect(jsonPath("$.takerOrderId").value(1L))
                .andExpect(jsonPath("$.makers[0].orderId").value(2L))
                .andExpect(jsonPath("$.makers[0].matchedQuantity").value(10L))
                .andExpect(jsonPath("$.price").value(1000L))
                .andExpect(jsonPath("$.totalMatchedQuantity").value(10L));
    }

    @Test
    void 주문_취소_요청을_처리하고_취소_결과를_반환한다() throws Exception {
        // given
        CancelRequest request = new CancelRequest(10L, 1L, BUY, 10000L);

        OrderResponse response =
                new OrderResponse(CANCELLED, null, List.of(), 0L, 0L);

        given(orderService.cancelOrder(any(CancelRequest.class)))
                .willReturn(response);

        // when & then
        mockMvc.perform(delete("/api/v1/market/order")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matchResult").value("CANCELLED"))
                .andExpect(jsonPath("$.takerOrderId").isEmpty())
                .andExpect(jsonPath("$.makers").isArray())
                .andExpect(jsonPath("$.makers").isEmpty())
                .andExpect(jsonPath("$.price").value(0L))
                .andExpect(jsonPath("$.totalMatchedQuantity").value(0L));
    }
}