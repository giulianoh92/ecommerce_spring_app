package com.example.main.error;

public class CustomError extends RuntimeException {
    private int code;
    private String message;
    private String detail;
    private int httpStatusCode;

    public CustomError(int code, String detail) {
        super(detail);
        this.code = code;
        ErrorMessages.ErrorDetail errorDetail = ErrorMessages.getErrorDetail(code);
        this.message = errorDetail.getMessage();
        this.httpStatusCode = errorDetail.getHttpStatusCode();
        this.detail = detail;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDetail() {
        return detail;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}