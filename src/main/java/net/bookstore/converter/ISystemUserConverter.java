package net.bookstore.converter;

import net.bookstore.converter.transferObj.SystemUserRequest;
import net.bookstore.converter.transferObj.SystemUserResponse;
import net.bookstore.entity.Role;
import net.bookstore.entity.SystemUser;

public interface ISystemUserConverter {
    SystemUser convertToEntity(SystemUserRequest userRequest, String normalizedPhoneNumber, Role userRole);

    SystemUserResponse systemUserToResponse(SystemUser systemUser);
}
