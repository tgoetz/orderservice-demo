package com.roche.orderservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(CustomizedResponseEntityExceptionHandler.class);

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
    logger.error("Handling exception", ex);
    return new ExceptionResponseEntity("Internal server error", request, INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ProductNotFoundException.class)
  public final ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException ex, WebRequest request) {
    return new ExceptionResponseEntity(ex.getMessage(), request, NOT_FOUND);
  }

  @ExceptionHandler(OrderInvalidException.class)
  public final ResponseEntity<Object> handleOrderInvalidException(OrderInvalidException ex, WebRequest request) {
    return new ExceptionResponseEntity(ex.getMessage(), request, BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    // Get all errors
    String errors = ex.getBindingResult().getFieldErrors().stream().
            map(DefaultMessageSourceResolvable::getDefaultMessage).
            collect(joining(", "));
    return new ExceptionResponseEntity(errors, request, BAD_REQUEST);
  }

}
