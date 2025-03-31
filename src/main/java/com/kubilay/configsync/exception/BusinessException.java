package com.kubilay.configsync.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException{

    private final HttpStatus status;

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public BusinessException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ServiceException {
        CONFIG_NOT_FOUND("Config not found.", HttpStatus.BAD_REQUEST),
        CONFIG_FILE_COULD_NOT_READ("Config file could not be read.", HttpStatus.BAD_REQUEST);

        private final String key;
        private final HttpStatus status;
    }
}
