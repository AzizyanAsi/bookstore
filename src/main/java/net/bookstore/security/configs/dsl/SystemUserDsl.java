package net.bookstore.security.configs.dsl;

import net.bookstore.common.util.LocaleUtils;
import net.bookstore.security.jwt.auth.SystemUserJwtAuthenticationFilter;
import net.bookstore.security.jwt.verify.JwtVerifier;
import net.bookstore.security.service.common.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class SystemUserDsl extends AbstractHttpConfigurer<SystemUserDsl, HttpSecurity> {

    private final JwtService jwtService;

    private final LocaleUtils localeUtils;

    public SystemUserDsl(JwtService jwtService, LocaleUtils localeUtils) {
        this.jwtService = jwtService;
        this.localeUtils = localeUtils;
    }

    @Override
    public void configure(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilterBefore(new SystemUserJwtAuthenticationFilter(authenticationManager, jwtService, localeUtils), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtVerifier(jwtService), SystemUserJwtAuthenticationFilter.class);
    }

    public static SystemUserDsl systemUserDsl(JwtService jwtService, LocaleUtils localeUtils) {
        return new SystemUserDsl(jwtService, localeUtils);
    }

}
