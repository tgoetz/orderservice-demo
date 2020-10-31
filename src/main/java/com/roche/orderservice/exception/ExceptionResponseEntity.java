package com.roche.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

public class ExceptionResponseEntity extends ResponseEntity<Object> {

    public ExceptionResponseEntity(String message, WebRequest request, HttpStatus status) {
        super(new ExceptionResponse(message, request, status), status);
    }

    public static class ExceptionResponse {

        private final LocalDateTime timestamp;
        private final String message;
        private final String details;
        private final String status;

        public ExceptionResponse(String message, WebRequest request, HttpStatus status) {
            this.timestamp = LocalDateTime.now();
            this.message = message;
            this.details = request.getDescription(false);
            this.status = status.value() + " (" + status.name() + ")";
        }

        public LocalDateTime getTimestamp() { return timestamp; }
        public String getMessage() { return message; }
        public String getDetails() { return details; }
        public String getStatus() { return status; }
    }

}
