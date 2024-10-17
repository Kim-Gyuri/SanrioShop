package com.example.demoshop.exception.token;

/**
 * status -> 404 오류
 */
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {
        super(message);
    }


}
