package com.fuzekun.common;

import lombok.Data;

/**
 * @author: Zekun Fu
 * @date: 2024/4/29 22:36
 * @Description:
 */
@Data
public class ResponseResult<T> {
    private int statusCode;
    private String message;
    private T data;
    public static final int ok = 200;
    public static final int error = 400;

    private ResponseResult(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    // Getters

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    // Builder class

    public static class Builder<T> {
        private int statusCode;
        private String message;
        private T data;

        public Builder<T> statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ResponseResult<T> build() {
            return new ResponseResult<>(statusCode, message, data);
        }
    }

    // Static helper methods for common responses

    public static Builder<String> ok(String message) {
        return new Builder<String>().statusCode(200).message(message);
    }

    public static Builder<String> error(String message) {
        return new Builder<String>().statusCode(400).message(message);
    }
}
