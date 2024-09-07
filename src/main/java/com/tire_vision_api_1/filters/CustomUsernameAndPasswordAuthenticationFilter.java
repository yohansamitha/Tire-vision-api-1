package com.tire_vision_api_1.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tire_vision_api_1.dto.tokens.TokenDTO;
import com.tire_vision_api_1.dto.user.LoginDTO;
import com.tire_vision_api_1.utils.functions.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

public class CustomUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(com.tire_vision_api_1.filters.CustomUsernameAndPasswordAuthenticationFilter.class);

    private static final String TOKEN = "jwtToken";
    private static final String USERNAME = "username";
    private static final String USERAVATAR = "userAvatar";
    private static final String USERID = "userId";
    private static final String USEREMAIL = "userEmail";
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public CustomUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            LoginDTO authenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginDTO.class);
            try {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        new String(decoder.decode(authenticationRequest.getPassword()))
                );

                return authenticationManager.authenticate(authentication);
            } catch (BadCredentialsException bce) {
                logger.error("Exception  :  ", bce);
                throw bce;
            } catch (AuthenticationException ex) {
                SecurityContextHolder.clearContext();
                throw ex;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {

        try {
            Date loggingDate = new Date();
            TokenDTO tokenDTO = jwtUtil.getTokensDTO(authResult.getName(), loggingDate);

            Map<String, Object> responseObject = new HashMap<>();
            responseObject.put("code", 200);
            responseObject.put(USERNAME, tokenDTO.getUserName());
            responseObject.put(TOKEN, tokenDTO.getToken());
            responseObject.put(USERAVATAR, tokenDTO.getUserAvatar());
            responseObject.put(USERID, tokenDTO.getUserID());
            responseObject.put(USEREMAIL, tokenDTO.getUserEmail());

            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(200);
            response.getWriter().write(objectMapper.writeValueAsString(responseObject));
        } catch (Exception e) {
            logger.error("Exception : ", e);
            throw e;
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        Map<String, Object> error = new HashMap<>();
        error.put("message", failed.getLocalizedMessage());
        error.put("code", HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
        SecurityContextHolder.clearContext();
    }

    @Override
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler failureHandler) {
        super.setAuthenticationFailureHandler(failureHandler);
    }
}
