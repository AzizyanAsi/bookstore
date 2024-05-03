package net.bookstore.security.configs;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import net.bookstore.security.configs.dsl.SystemUserDsl;
import net.bookstore.common.util.LocaleUtils;
import net.bookstore.security.service.SystemUserDetailsService;
import net.bookstore.security.service.common.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static net.bookstore.common.data.StringConstants.AUTHORIZATION;
import static net.bookstore.common.data.StringConstants.REFRESH_TOKEN;
import static net.bookstore.entity.enums.RoleType.ADMIN;
import static net.bookstore.entity.enums.RoleType.REGULAR_USER;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ApplicationSecurityConfig {

    private final SystemUserDetailsService systemUserDetailsService;
    private final JwtService jwtService;
    private final LogoutHandler logoutHandler;
    private PasswordEncoder passwordEncoder;
    private CorsConfig corsConfig;
    private LocaleUtils localeUtils;
    private Environment environment;

    public ApplicationSecurityConfig(SystemUserDetailsService systemUserDetailsService,
                                     JwtService jwtService,
                                     LogoutHandler logoutHandler) {
        this.systemUserDetailsService = systemUserDetailsService;
        this.jwtService = jwtService;
        this.logoutHandler = logoutHandler;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setCorsConfig(CorsConfig corsConfig) {
        this.corsConfig = corsConfig;
    }

    @Autowired
    public void setLocaleUtils(LocaleUtils localeUtils) {
        this.localeUtils = localeUtils;
    }

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public SecurityFilterChain systemUserFilterChain(HttpSecurity http) throws Exception {

        http.securityMatcher("/system/**")
                .authorizeHttpRequests((registry) -> registry
                        .requestMatchers(HttpMethod.GET,
                                "/system/actuator/health"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "system/token/refresh",
                                "system/systemUser/registration"
                        ).permitAll()
                        .requestMatchers("/system/roles").hasRole(ADMIN.name())
                        .requestMatchers("/system/genre").hasRole(ADMIN.name())
                        .requestMatchers("/system/author").hasRole(ADMIN.name())
                        .requestMatchers("/system/**").hasAnyRole(ADMIN.name(), REGULAR_USER.name())
                        .anyRequest().authenticated()
                );

        http.apply(SystemUserDsl.systemUserDsl(jwtService, localeUtils));

        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.headers((headers) -> headers
                .xssProtection(Customizer.withDefaults())
                .contentSecurityPolicy((contentSecurityPolicy) -> contentSecurityPolicy.policyDirectives("script-src 'self'")));

        http.logout((logout) -> logout
                .logoutUrl("/system/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));

        http.authenticationProvider(systemUserAuthenticationProvider());

        return http.build();
    }


    @Bean
    public AuthenticationProvider systemUserAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(systemUserDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .authenticationProvider(systemUserAuthenticationProvider());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();

        // Set origins depending on profile
        String[] profiles = environment.getActiveProfiles();
        // Allow all if active profile is 'dev', otherwise - fetch from config 
        if (profiles.length > 0 && Arrays.asList(profiles).contains("dev")) {
            configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        } else {
            configuration.setAllowedOrigins(corsConfig.getAllowedOrigins());
        }
        configuration.setAllowedMethods(ImmutableList.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(ImmutableList.of(AUTHORIZATION, REFRESH_TOKEN, "Cache-Control", "Content-Type"));
        configuration.setExposedHeaders(ImmutableList.of(AUTHORIZATION, REFRESH_TOKEN));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        log.info("CORS is enabled (see active profile)");
        return source;
    }
}
