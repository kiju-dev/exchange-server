package com.exchange.exchange_server.order.controller;

import com.exchange.exchange_server.order.controller.request.CancelRequest;
import com.exchange.exchange_server.order.controller.request.OrderRequest;
import com.exchange.exchange_server.order.controller.response.OrderResponse;
import com.exchange.exchange_server.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/market")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<OrderResponse> order(@RequestBody OrderRequest request) {
        OrderResponse response = orderService.placeOrder(request);
        return ResponseEntity.status(OK).body(response);
    }

    @DeleteMapping("/order")
    public ResponseEntity<OrderResponse> cancel(@RequestBody CancelRequest request) {
        OrderResponse response = orderService.cancelOrder(request);
        return ResponseEntity.status(OK).body(response);
    }
}
