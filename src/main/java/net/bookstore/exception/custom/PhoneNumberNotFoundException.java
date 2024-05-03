package net.bookstore.exception.custom;

import org.springframework.security.core.AuthenticationException;

public class PhoneNumberNotFoundException extends AuthenticationException {

    public PhoneNumberNotFoundException(String msg) {
        super(msg);
    }
}
