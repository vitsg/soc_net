package com.homework.social_network.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final String code;

    public CustomException(String code) {
        this.code = code;
    }

    public CustomException(String code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
