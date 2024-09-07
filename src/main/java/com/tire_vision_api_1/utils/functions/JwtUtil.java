package com.tire_vision_api_1.utils.functions;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tire_vision_api_1.dto.tokens.TokenDTO;
import com.tire_vision_api_1.dto.tokens.TokenPayLoadDTO;
import com.tire_vision_api_1.repository.UserRepository;
import com.tire_vision_api_1.utils.constant.JWTConfig;
import com.tire_vision_api_1.utils.mapping.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Date;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private final JWTConfig jwtConfig;
    private final UserRepository userRepository;

    @Autowired
    public JwtUtil(JWTConfig jwtConfig, UserRepository userRepository) {
        this.jwtConfig = jwtConfig;
        this.userRepository = userRepository;
    }

    public String getUsernameFromToken(String token) {
        if (token != null) {
            String payLoad = JWT.require(
                    Algorithm.HMAC512(jwtConfig.getSecretKey().getBytes()))
                    .build().verify(token).getSubject();
            Date expiresAt = JWT.require(
                    Algorithm.HMAC512(jwtConfig.getSecretKey().getBytes()))
                    .build().verify(token).getExpiresAt();
            if (payLoad != null && !payLoad.isEmpty()) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                TokenPayLoadDTO payLoadBean = gson.fromJson(payLoad, TokenPayLoadDTO.class);
                payLoadBean.setExpiresTime(expiresAt);
                return payLoadBean.getUserName();
            }
        }
        return null;
    }

    public TokenDTO getTokensDTO(String username, Date loggingTime) {
        TokenDTO refData = new TokenDTO();
        try {
            User optionalUser = userRepository.findByUsername(username).orElseThrow();
            Date tokenExpireDate = new Date(loggingTime.getTime() + jwtConfig.getTokenExp());
            refData.setToken(this.getToken(username, tokenExpireDate));
            refData.setUserName(optionalUser.getFirstName() + " " + optionalUser.getLastName());
            refData.setUserAvatar(CommonFunctions.getStringImage(optionalUser.getUserAvatar()));
            refData.setUserID(String.valueOf(optionalUser.getUserId()));
            refData.setUserEmail(optionalUser.getEmail());
            return refData;
        } catch (Exception ex) {
            logger.error("Exception : ", ex);
            throw ex;
        }
    }

    private String getToken(String username, Date tokenExpireDate) {
        try {
            String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
            TokenPayLoadDTO payLoadBean = new TokenPayLoadDTO(sessionId, username, tokenExpireDate);

            Gson gson = new Gson();
            String payLoadJson = gson.toJson(payLoadBean);

            String token = JWT.create()
                    .withSubject(payLoadJson)
                    .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getTokenExp()))
                    .sign(Algorithm.HMAC512(jwtConfig.getSecretKey().getBytes()));

            token = jwtConfig.getTokenPrefix() + token;
            return token;
        } catch (Exception ex) {
            logger.error("Exception : ", ex);
            throw ex;
        }
    }
}
