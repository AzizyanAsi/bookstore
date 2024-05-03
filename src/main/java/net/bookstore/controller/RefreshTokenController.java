package net.bookstore.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.bookstore.common.api.ApiResponse;
import net.bookstore.service.entity.SystemUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("systemUserRefreshTokenController")
@RequestMapping("/system/token")
public class RefreshTokenController {

    private final SystemUserService systemUserService;

    public RefreshTokenController(SystemUserService systemUserService) {
        this.systemUserService = systemUserService;
    }

    @PostMapping("refresh")
    public ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        systemUserService.refreshTokens(request, response);
        return ApiResponse.ok().build();
    }

}
