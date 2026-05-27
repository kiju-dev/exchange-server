package com.exchange.exchange_server.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    MARKET_ALREADY_OPEN(HttpStatus.CONFLICT, "MARKET_001", "이미 개장 상태입니다."),
    MARKET_ALREADY_CLOSE(HttpStatus.CONFLICT, "MARKET_002", "이미 폐장 상태입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_001", "서버 내부에 오류가 발생했습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
