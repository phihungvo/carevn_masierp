package com.carevn.masi.dto;

public class Response {
    private String message;
    private boolean success;
    private Object error;
    private Object data;

    public Response() {
    }

    public Response(String message, boolean success, Object error, Object data) {
        this.message = message;
        this.success = success;
        this.error = error;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static Response success() {
        return new Response(null, true, null, null);
    }

    public static Response success(String message, Object data) {
        return new Response(message, true, null, data);
    }

    public static Response error(String message, Object error) {
        return new Response(message, false, error, null);
    }
}
