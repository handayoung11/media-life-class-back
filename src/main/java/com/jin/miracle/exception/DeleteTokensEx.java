package com.jin.miracle.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.HttpClientErrorException;

@Getter
@RequiredArgsConstructor
public class DeleteTokensEx extends RuntimeException {
    private final HttpClientErrorException exeption;
}