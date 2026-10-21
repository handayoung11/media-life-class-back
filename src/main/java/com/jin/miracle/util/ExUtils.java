package com.jin.miracle.util;

import org.springframework.http.HttpStatus;
import org.springframework.util.SerializationUtils;
import org.springframework.web.client.HttpClientErrorException;

public class ExUtils {
    public static HttpClientErrorException makeHttpClientErrorEx(HttpStatus status, Object object) {
        try {
            byte[] serialize = SerializationUtils.serialize(object);
            return new HttpClientErrorException(status, "", serialize, null);
        } catch (Exception e) {
            e.fillInStackTrace();
            return new HttpClientErrorException(status, "");
        }
    }
}
