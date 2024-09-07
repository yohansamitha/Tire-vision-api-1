package com.tire_vision_api_1.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.tire_vision_api_1.services.user.UserService;
import com.tire_vision_api_1.utils.constant.JWTConfig;
import com.tire_vision_api_1.utils.functions.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Component
public class JwtFilter extends BasicAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final UserService userDetailService;
    private final JWTConfig jwtConfig;
    private final ObjectMapper objectMapper;

    public JwtFilter(AuthenticationManager authenticationManager, UserService userDetailService, JWTConfig jwtConfig, JwtUtil jwtUtil, ObjectMapper objectMapper) {
        super(authenticationManager);
        this.userDetailService = userDetailService;
        this.jwtConfig = jwtConfig;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");
        try {

            String username = jwtUtil.getUsernameFromToken(token);

            UserDetails userDetails = userDetailService.loadUserByUsername(username);
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }
}
