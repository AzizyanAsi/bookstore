package net.bookstore.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import net.bookstore.entity.SystemUser;
import net.bookstore.common.api.ApiResponse;
import net.bookstore.common.util.LocaleUtils;
import net.bookstore.service.entity.SystemUserService;
import net.bookstore.converter.transferObj.SystemUserRequest;
import net.bookstore.converter.transferObj.SystemUserResponse;
import net.bookstore.converter.ISystemUserConverter;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@Validated
@RestController
@RequestMapping("system/systemUser")
public class SystemUserController {
    private final SystemUserService systemUserService;
    private final ISystemUserConverter systemUserConverter;
    private final LocaleUtils localeUtils;

    public SystemUserController(SystemUserService systemUserService, ISystemUserConverter systemUserConverter,
            LocaleUtils localeUtils) {
        this.systemUserService = systemUserService;
        this.systemUserConverter = systemUserConverter;
        this.localeUtils = localeUtils;
    }

    @PostMapping("registration")
    public ApiResponse<SystemUserResponse> registerSystemUser(@Valid @RequestBody SystemUserRequest userRequest) {
        SystemUser systemUser = systemUserService.registerSystemUser(userRequest);
        var responseUser = systemUserConverter.systemUserToResponse(systemUser);
        return ApiResponse.ok(localeUtils.getLocalizedMessage("success.user.register"), responseUser);
    }


    @GetMapping("{id}")
    public ApiResponse<SystemUserResponse> getSystemUser(
            @PathVariable("id") @Positive(message = "{validation.positive}") Long id) {
        var systemUser = systemUserConverter.systemUserToResponse(systemUserService.getEntity(id));
        return ApiResponse.ok(localeUtils.getLocalizedMessage("success.entity.found"),
                systemUser);
    }


    @GetMapping(params = { "email" })
    public ApiResponse<SystemUser> getSystemUserByEmail(
            @RequestParam("email") @Email(message = "email is not valid") String email) {
        SystemUser systemUser = systemUserService.getActiveUserByEmail(email);
        return ApiResponse.ok(localeUtils.getLocalizedMessage("success.entity.found"), systemUser);
    }

}
