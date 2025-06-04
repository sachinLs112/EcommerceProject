package com.ecom.ecommerce.api.v1.exception;


import jakarta.ws.rs.core.Response;

public class EcommerceException extends Exception {
    private final Response.Status httpStatus;
    private final String errorCode;
    private final String errorMsg;


    public Response.Status getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage(){
        return errorMsg;
    }

    public EcommerceException(Response.Status status, String errorCode, String errorMsg) {

        if (errorMsg == null || errorMsg.isEmpty()) {
            errorMsg = "Error Occurred";
        }
        String finalErrorMsg;
        this.httpStatus = status;
        this.errorCode = errorCode;
        try {
            finalErrorMsg = errorMsg;
        } catch (Exception ex) {
            finalErrorMsg=errorMsg;
        }
        this.errorMsg = finalErrorMsg;

    }
}
