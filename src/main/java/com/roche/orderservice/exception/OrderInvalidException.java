package com.roche.orderservice.exception;

public class OrderInvalidException extends RuntimeException {

    public OrderInvalidException(String message) {
        super(message);
    }

}
