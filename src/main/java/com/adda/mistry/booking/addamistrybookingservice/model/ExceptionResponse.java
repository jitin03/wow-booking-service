package com.adda.mistry.booking.addamistrybookingservice.model;

import org.springframework.http.HttpStatus;

public class ExceptionResponse {
    private String errorMessage;
    private HttpStatus statudCode;
    private String requestedURI;

    public HttpStatus getStatudCode() {
        return statudCode;
    }

    public void setStatudCode(HttpStatus statudCode) {
        this.statudCode = statudCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getRequestedURI() {
        return requestedURI;
    }

    public void callerURL(final String requestedURI) {
        this.requestedURI = requestedURI;
    }
}
