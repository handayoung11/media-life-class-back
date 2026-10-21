package com.jin.miracle.exception;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class LogoutErrRes implements Serializable {
    private final String msg;
    private final boolean doLogout = true;

    public LogoutErrRes(String msg) {
        this.msg = msg;
    }
}