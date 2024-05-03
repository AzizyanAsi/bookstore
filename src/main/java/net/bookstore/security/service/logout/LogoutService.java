package net.bookstore.security.service.logout;

import com.google.common.base.Strings;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.bookstore.common.data.StringConstants;
import net.bookstore.security.service.common.JwtService;
import net.bookstore.security.service.common.LoggedUserStorageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogoutService implements LogoutHandler {

    private final JwtService jwtService;
    private final LoggedUserStorageService loggedSystemUserStorageService;

    public LogoutService(JwtService jwtService,
                         @Qualifier("loggedSystemUserStorageServiceImpl") LoggedUserStorageService loggedSystemUserStorageService) {
        this.jwtService = jwtService;
        this.loggedSystemUserStorageService = loggedSystemUserStorageService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authorizationHeader = request.getHeader(StringConstants.AUTHORIZATION);
        if (!Strings.isNullOrEmpty(authorizationHeader) && authorizationHeader.startsWith(StringConstants.JWT_PREFIX)) {
            String accessToken = authorizationHeader.substring(StringConstants.JWT_PREFIX.length()).stripLeading();
            try {
                Authentication auth = jwtService.decodeAccessJwt(accessToken, request);
                String email = (String) auth.getPrincipal();
                loggedSystemUserStorageService.interruptUserSession(email);
                response.setStatus(HttpStatus.OK.value());
            } catch (JwtException exception) {
                log.warn("Error while trying to logout: {}", exception.getMessage());
                response.setStatus(HttpStatus.BAD_REQUEST.value());
            }
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }
}
