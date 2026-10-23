package com.jin.miracle.exception;

import com.jin.miracle.util.CookieUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class, HttpMessageNotReadableException.class})
    public ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException e) {
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

    @ExceptionHandler(DeleteTokensEx.class)
    public ResponseEntity<Object> handleAuthException(DeleteTokensEx e, HttpServletResponse resp) throws Exception {
        CookieUtils.deleteTokens(resp);
        return handleHttpClientErrorException(e.getExeption());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class, MissingServletRequestPartException.class})
    public ResponseEntity<String> handleMethodArgumentException() {
        return new ResponseEntity<>(
                "parameter 명이나 type, value, 개수가 올바른지 확인해주세요"
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        if (!e.getClass().equals(MethodArgumentNotValidException.class)) {
            log.info("e.getclass is {}", e.getClass());
            return handleMethodArgumentException();
        }

        String errorMessage = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxUploadSizeExceedException(MaxUploadSizeExceededException e) {
        return new ResponseEntity<>("파일의 용량이 너무 큽니다", HttpStatus.BAD_REQUEST);
    }
}
