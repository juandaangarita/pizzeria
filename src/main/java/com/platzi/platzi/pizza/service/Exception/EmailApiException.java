package com.platzi.platzi.pizza.service.Exception;

public class EmailApiException extends RuntimeException{
    public EmailApiException(){
        super("Error sending email...");
    }
}
