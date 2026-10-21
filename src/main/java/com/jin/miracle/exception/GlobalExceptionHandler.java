package com.jin.miracle.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class, HttpMessageNotReadableException.class})
    public Object handleException(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public Object handleHttpClientErrorException(HttpClientErrorException e) {
        byte[] bytes = e.getResponseBodyAsByteArray();
        if (bytes.length == 0)
            return new ResponseEntity<>(e.getStatusText(), e.getStatusCode());
        else {
            try {
                Object response = SerializationUtils.deserialize(bytes);
                return new ResponseEntity<>(response, e.getStatusCode());
            } catch (Exception ex) {
                return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
            }
        }
    }
}
