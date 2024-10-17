package com.example.demoshop.exception.token;

/**
 * status -> 404 오류
 */
public class InvalidRefreshTokenException extends RuntimeException {

    public InvalidRefreshTokenException(String message) {
        super(message);
    }


}
