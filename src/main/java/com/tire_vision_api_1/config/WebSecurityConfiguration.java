package com.tire_vision_api_1.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tire_vision_api_1.filters.CustomUsernameAndPasswordAuthenticationFilter;
import com.tire_vision_api_1.filters.JwtFilter;
import com.tire_vision_api_1.services.user.UserService;
import com.tire_vision_api_1.utils.constant.JWTConfig;
import com.tire_vision_api_1.utils.constant.SecurityConfigurationProperties;
import com.tire_vision_api_1.utils.functions.JwtUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {

    public static final Logger logger = LoggerFactory.getLogger(com.tire_vision_api_1.config.WebSecurityConfiguration.class);
    private static final String[] AUTH_WHITELIST = {
            "/login/**",
            "/api/v1/user/register",
            "/api/v1/user/forgot-password",
            "/api/v1/user/reset-password",
            "/api/v1/guest/tire-check",
    };

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SecurityConfigurationProperties configurationProperties;
    @Autowired
    @Lazy
    private UserService userDetailService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private JWTConfig jwtConfig;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exp -> exp.authenticationEntryPoint(restAuthenticationEntryPoint()))
                .authorizeRequests(auth -> auth
                        .antMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider())
                .addFilter(new CustomUsernameAndPasswordAuthenticationFilter(authenticationManager(),
                        jwtUtil, objectMapper))
                .addFilterBefore(new JwtFilter(authenticationManager(), userDetailService,
                        jwtConfig, jwtUtil, objectMapper), UsernamePasswordAuthenticationFilter.class)
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .logoutSuccessHandler(logoutSuccessHandler())
                                .permitAll()
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        logger.info("CORS Configuration : " + configurationProperties.toString());
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:4200", "*"));
//        configuration.setAllowedOrigins(configurationProperties.getAllowedOrigins());
        configuration.setAllowedMethods(configurationProperties.getAllowedMethods());
        configuration.setAllowedHeaders(configurationProperties.getAllowedHeaders());
        configuration.setAllowCredentials(configurationProperties.isAllowedCredentials());
        configuration.setExposedHeaders(configurationProperties.getExposedHeaders());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return (httpServletRequest, httpServletResponse, e) -> {
            Map<String, Object> errorObject = new HashMap<>();
            errorObject.put("message", "Unauthorized access of protected resource, invalid credentials");
            errorObject.put("error", HttpStatus.UNAUTHORIZED);
            errorObject.put("code", 401);
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(errorObject));

        };
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
