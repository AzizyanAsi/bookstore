package net.bookstore.service.entity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.bookstore.entity.Role;
import net.bookstore.entity.SystemUser;
import net.bookstore.converter.transferObj.SystemUserRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

public interface SystemUserService extends EntityReadService<SystemUser> {
    SystemUser registerSystemUser(SystemUserRequest userRequest);

    SystemUser getActiveUserByEmail(String email) throws AuthenticationException;

    SystemUser getActiveUserByPhoneNumber(String phoneNumber) throws AuthenticationException;

    Set<SimpleGrantedAuthority> getAuthorities(Role role);

    void refreshTokens(HttpServletRequest request, HttpServletResponse response);

}
