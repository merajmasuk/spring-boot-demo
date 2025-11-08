package com.example.springtutorial.exceptions;

import lombok.Getter;

@Getter
public class BusinessException extends Exception {
    public String errorCode;

    public BusinessException(String errorCode, String error) {
        super(error);
        this.errorCode = errorCode;
    }
}
