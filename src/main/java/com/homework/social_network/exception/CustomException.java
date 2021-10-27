package com.homework.social_network.exception;

import com.homework.social_network.enums.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode code;

    public CustomException(ErrorCode code) {
        this.code = code;
    }

    public CustomException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(ErrorCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
