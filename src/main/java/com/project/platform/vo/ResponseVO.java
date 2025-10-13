package com.project.platform.vo;

public class ResponseVO<T> {
    private int code;
    private String msg;
    private T data;

    public ResponseVO(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResponseVO<T> ok() {
        return new ResponseVO<>(200, "操作成功", null);
    }

    public static <T> ResponseVO<T> ok(T data) {
        return new ResponseVO<>(200, "操作成功", data);
    }

    public static <T> ResponseVO<T> ok(String message, T data) {
        return new ResponseVO<>(200, message, data);
    }

    public static <T> ResponseVO<T> fail(int code, String message) {
        return new ResponseVO<>(code, message, null);
    }

    public static <T> ResponseVO<T> fail(int code, String message, T data) {
        return new ResponseVO<>(code, message, data);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
