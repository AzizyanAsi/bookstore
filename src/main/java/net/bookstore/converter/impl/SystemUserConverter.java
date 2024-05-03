package net.bookstore.converter.impl;

import lombok.RequiredArgsConstructor;
import net.bookstore.converter.ISystemUserConverter;
import net.bookstore.entity.Role;
import net.bookstore.entity.SystemUser;
import net.bookstore.converter.transferObj.SystemUserRequest;
import net.bookstore.converter.transferObj.SystemUserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SystemUserConverter implements ISystemUserConverter {
    private final PasswordEncoder passwordEncoder;
    private final ResponseMappers responseMappers;

    @Override
    public SystemUser convertToEntity(SystemUserRequest userRequest, String normalizedPhoneNumber, Role userRole) {
        return Optional.ofNullable(userRequest).map(request -> {
            SystemUser user = new SystemUser();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setPhoneNumber(normalizedPhoneNumber);
            user.setActive(true);
            user.setRole(userRole);
            return user;
        }).orElse(null);
    }


    @Override
    public SystemUserResponse systemUserToResponse(SystemUser systemUser) {
        return Optional.ofNullable(systemUser).map(user -> {
            SystemUserResponse systemUserResponse = new SystemUserResponse();
            systemUserResponse.setId(user.getId());
            systemUserResponse.setFirstName(user.getFirstName());
            systemUserResponse.setLastName(user.getLastName());
            systemUserResponse.setEmail(user.getEmail());
            systemUserResponse.setPhoneNumber(user.getPhoneNumber());
            systemUserResponse.setActive(user.getActive());
            systemUserResponse.setRole(responseMappers.roleToResponse(user.getRole()));
            systemUserResponse.setCreated(user.getCreated());
            systemUserResponse.setUpdated(user.getUpdated());
            return systemUserResponse;
        }).orElse(null);
    }

}
