package com.example.springtutorial.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse<T> {
    private Integer status;
    private T data;
    private String errorCode;
    private String error;

    public static <T> BaseResponse<T> ok(Integer status, T data) {
        return new BaseResponse<>(status, data, null, null);
    }
}
