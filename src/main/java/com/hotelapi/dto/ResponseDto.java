package com.hotelapi.dto;

public class ResponseDto {
    private String message;
    private boolean success;

    public ResponseDto(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
//need to edit