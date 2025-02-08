package com.ritik.backend.response;


import lombok.Data;

@Data
public class ApiResponse {

    private String message;
    private Object object;

    public ApiResponse(String message, Object object){
        this.message = message;
        this.object = object;
    }
}
