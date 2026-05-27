package com.exchange.exchange_server.market;

import com.exchange.exchange_server.global.exception.CustomException;
import com.exchange.exchange_server.market.controller.response.ExchangeStatus;
import lombok.Getter;
import org.springframework.stereotype.Component;

import static com.exchange.exchange_server.global.exception.ErrorCode.MARKET_ALREADY_CLOSE;
import static com.exchange.exchange_server.global.exception.ErrorCode.MARKET_ALREADY_OPEN;
import static com.exchange.exchange_server.market.controller.response.ExchangeStatus.RUNNING;
import static com.exchange.exchange_server.market.controller.response.ExchangeStatus.STOPPED;

@Component
@Getter
public class ExchangeState {

    private ExchangeStatus status = STOPPED;

    public boolean isRunning() {
        return status == RUNNING;
    }

    public void open() {
        if (status == RUNNING) {
            throw new CustomException(MARKET_ALREADY_OPEN);
        }
        status = RUNNING;
    }

    public void close() {
        if (status == STOPPED) {
            throw new CustomException(MARKET_ALREADY_CLOSE);
        }
        status = STOPPED;
    }
}
