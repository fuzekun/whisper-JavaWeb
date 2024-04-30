package com.fuzekun.dto;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;

/**
 * @author: fuzekun
 * @date: 2024/4/29
 * @describe:
 */

public class ResponseResult<T> implements Serializable {
    private Boolean status;
    private String message;
    private T data;


    public ResponseResult(Boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseResult() {
    }

    public Boolean getStatus() {
        return this.status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toString() {
        return "ResponseResult{status='" + this.status + '\'' + ", message='" + this.message + '\'' + ", data=" + this.data;
    }

    public static class Builder<T> {
        private static final String SUCCESS_MSG = "操作成功";
        private static final String ERROR_MSG = "操作失败";

        public Builder() {
        }

        private ResponseResult build() {
            ResponseResult responseResult = new ResponseResult();
            return responseResult;
        }

        public static ResponseResult buildOk() {
            ResponseResult responseResult = new ResponseResult();
            responseResult.setMessage("操作成功");
            responseResult.setStatus(true);
            return responseResult;
        }

        public static ResponseResult buildOk(String message) {
            ResponseResult responseResult = new ResponseResult();
            responseResult.setMessage(message);
            responseResult.setStatus(true);
            return responseResult;
        }

        public static <T> ResponseResult buildOk(String message, T data) {
            ResponseResult responseResult = new ResponseResult();
            responseResult.setMessage(message);
            responseResult.setStatus(true);
            responseResult.setData(data);
            return responseResult;
        }

        public static ResponseResult buildError() {
            ResponseResult responseResult = new ResponseResult();
            responseResult.setMessage("操作失败");
            responseResult.setStatus(false);
            return responseResult;
        }

        public static ResponseResult buildError(String message) {
            ResponseResult responseResult = new ResponseResult();
            responseResult.setMessage(message);
            responseResult.setStatus(false);
            return responseResult;
        }

        public static <T> ResponseResult buildError(String message, T data) {
            ResponseResult responseResult = new ResponseResult();
            responseResult.setMessage(message);
            responseResult.setStatus(false);
            responseResult.setData(data);
            return responseResult;
        }
    }
}
