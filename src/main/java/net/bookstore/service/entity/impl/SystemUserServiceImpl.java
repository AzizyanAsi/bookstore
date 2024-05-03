package net.bookstore.service.entity.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.bookstore.exception.common.EntityNotFoundException;
import net.bookstore.exception.custom.AccountNotActiveException;
import net.bookstore.exception.custom.EmailNotFoundException;
import net.bookstore.exception.custom.PhoneNumberNotFoundException;
import net.bookstore.common.config.AppConfig;
import net.bookstore.common.data.CountryCode;
import net.bookstore.common.util.PhoneNumberUtils;
import net.bookstore.entity.Role;
import net.bookstore.entity.SystemUser;
import net.bookstore.repository.SystemUserRepository;
import net.bookstore.security.service.common.JwtService;
import net.bookstore.service.entity.RoleService;
import net.bookstore.service.entity.SystemUserService;
import net.bookstore.converter.transferObj.SystemUserRequest;
import net.bookstore.converter.ISystemUserConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static net.bookstore.common.cache.EntityCacheNames.ALL_SYSTEM_USERS;
import static net.bookstore.common.cache.EntityCacheNames.SYSTEM_USER;

@Slf4j
@Service
public class SystemUserServiceImpl implements SystemUserService {
    private final SystemUserRepository systemUserRepository;
    private final RoleService roleService;
    private final ISystemUserConverter systemUserConverter;

    private AppConfig appConfig;
    private JwtService jwtService;


    public SystemUserServiceImpl(SystemUserRepository systemUserRepository, RoleService roleService,
                                 ISystemUserConverter systemUserConverter) {
        this.systemUserRepository = systemUserRepository;
        this.roleService = roleService;
        this.systemUserConverter = systemUserConverter;
    }

    @Autowired
    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    @Override
    public SystemUser getActiveUserByEmail(String email) throws AuthenticationException {
        Optional<SystemUser> optSystemUser = systemUserRepository.findByEmailIgnoreCase(email);
        if (optSystemUser.isEmpty()) {
            throw new EmailNotFoundException(String.format("SystemUser with email '%s' not found", email));
        }
        SystemUser systemUser = optSystemUser.get();
        this.verifySystemUser(systemUser);
        return systemUser;
    }

    @Override
    public SystemUser getActiveUserByPhoneNumber(String phoneNumber) throws AuthenticationException {
        CountryCode countryCode = CountryCode.valueOf(appConfig.getAppCountryCode());
        boolean isValid = PhoneNumberUtils.validate(phoneNumber, countryCode);
        if (isValid) {
            phoneNumber = PhoneNumberUtils.normalize(phoneNumber, countryCode);
        } else {
            throw new PhoneNumberNotFoundException("Invalid phone number");
        }
        Optional<SystemUser> loadedUser = systemUserRepository.findByPhoneNumber(phoneNumber);
        if (loadedUser.isEmpty()) {
            throw new PhoneNumberNotFoundException(String.format("SystemUser with phone number '%s' not found", phoneNumber));
        }
        SystemUser user = loadedUser.get();
        this.verifySystemUser(user);
        return user;
    }

    @Override
    public Set<SimpleGrantedAuthority> getAuthorities(Role role) {
        // ROLE_ prefix is used by hasRole method of ExpressionUrlAuthorizationConfigurer framework class
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleType().toString()));
        return authorities;
    }

    @Override
    public void refreshTokens(HttpServletRequest request, HttpServletResponse response) {
        String email = jwtService.decodeRefreshJwt(request);
        SystemUser systemUser = this.getActiveUserByEmail(email);
        jwtService.provideTokens(email, this.getAuthorities(systemUser.getRole()), request, response);
    }



    /* -- PRIVATE METHODS -- */

    private void verifySystemUser(SystemUser systemUser) throws AuthenticationException {
        if (!systemUser.getActive().equals(true)) {
            String message = String.format("SystemUser account of '%s' is not active", systemUser.getEmail());
            throw new AccountNotActiveException(message);
        }
    }

    @Override
    public SystemUser registerSystemUser(SystemUserRequest userRequest) {
        // Normalize already validated phone number
        String normalizedPhoneNumber = PhoneNumberUtils.normalize(userRequest.getPhoneNumber(),
                CountryCode.valueOf(appConfig.getAppCountryCode()));
        Role userRole = roleService.getByRoleType(userRequest.getUserRole());
        var converted = systemUserConverter.convertToEntity(userRequest, normalizedPhoneNumber, userRole);
        var savedUser = systemUserRepository.save(converted);
        log.info("User successfully registered: {}", savedUser);
        return savedUser;
    }

    @Override
    @Cacheable(value = ALL_SYSTEM_USERS)
    public List<SystemUser> getAllEntities() throws AuthenticationException {
        return systemUserRepository.findAll();
    }

    @Override
    @Cacheable(value = SYSTEM_USER, key = "#id", unless = "#result == null")
    public SystemUser getEntity(Long id) throws AuthenticationException {
        Optional<SystemUser> loadedUser = systemUserRepository.findById(id);
        if (loadedUser.isEmpty()) {
            throw new EntityNotFoundException(String.format("User with id {%d} not found", id));
        }
        return loadedUser.get();
    }

}
