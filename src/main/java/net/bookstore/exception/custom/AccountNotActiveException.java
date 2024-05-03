package net.bookstore.exception.custom;

import org.springframework.security.core.AuthenticationException;

public class AccountNotActiveException extends AuthenticationException {

    public AccountNotActiveException(String msg) {
        super(msg);
    }
}
