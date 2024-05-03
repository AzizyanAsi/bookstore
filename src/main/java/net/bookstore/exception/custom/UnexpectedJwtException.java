package net.bookstore.exception.custom;

import io.jsonwebtoken.JwtException;

public class UnexpectedJwtException extends JwtException {
    public UnexpectedJwtException(String message) {
        super(message);
    }
}
