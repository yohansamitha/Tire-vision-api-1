package com.tire_vision_api_1.services.user.impl;

import com.tire_vision_api_1.dto.reponses.KeyValueDTO;
import com.tire_vision_api_1.dto.reponses.StandardResponse;
import com.tire_vision_api_1.dto.user.UserDTO;
import com.tire_vision_api_1.repository.UserRepository;
import com.tire_vision_api_1.repository.VerificationCodeRepository;
import com.tire_vision_api_1.services.APIServices.EmailService;
import com.tire_vision_api_1.services.user.UserService;
import com.tire_vision_api_1.utils.functions.CommonFunctions;
import com.tire_vision_api_1.utils.mapping.User;
import com.tire_vision_api_1.utils.mapping.VerificationCode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

/*
 * @author Yohan Samitha
 * @Project tire-vision-api-1
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));
    }

    @Override
    public StandardResponse insert(UserDTO userDTO) {
        StandardResponse response = new StandardResponse();
        Base64.Decoder decoder = Base64.getDecoder();
        Optional<User> byUsername = userRepository.findByUsername(userDTO.getEmail());
        if (byUsername.isEmpty()) {
            User user = this.mapper.map(userDTO, User.class);
            user.setRoles("ADMIN");
            user.setPassword(passwordEncoder.encode(new String(decoder.decode(userDTO.getPassword()))));
            userRepository.save(user);
//            cannot get the API key
//            emailService.sendHtmlMessage(userDTO.getEmail(), user.getFirstName() + user.getLastName());
            response.setCode("200");
            response.setMessage("User Registration Successful. Please Login Again!");
        } else {
            response.setCode("400");
            response.setMessage("Something Went Wrong. Please Try Again!");
        }
        return response;
    }

    @Override
    public StandardResponse update(UserDTO userDTO) {
        StandardResponse standardResponse = new StandardResponse();
        return standardResponse;
    }

    @Override
    public StandardResponse initiateForgotPassword(String email) {
        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            return new StandardResponse("400", "Email not found", null);
        }

        String verificationCode = generateVerificationCode();
        this.saveVerificationCode(user.getUserId(), user.getEmail(), verificationCode);

        String message = "Your verification code is: " + verificationCode;
        System.out.println(message);
        System.out.println("http://localhost:4200/auth/forget-password/enter-otp?otp=" + verificationCode);

//            cannot get the API key
//        emailService.sendForgetPasswordEmail(email, user.getFirstName() + user.getLastName(), verificationCode);

        return new StandardResponse("200", "Verification code sent to your email", null);
    }

    @Override
    @Transactional
    public StandardResponse resetPassword(String email, String verificationCode, String newPassword) {
        Base64.Decoder decoder = Base64.getDecoder();
        boolean isVerified = this.verifyVerificationCode(email, verificationCode);
        if (!isVerified) {
            return new StandardResponse("400", "Invalid verification code", null);
        }

        userRepository.resetPassword(email, passwordEncoder.encode(new String(decoder.decode(newPassword))));
        verificationCodeRepository.deleteByVerificationCode(verificationCode);

        return new StandardResponse("200", "Password reset successfully", null);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int num = 1000 + random.nextInt(9000);
        return String.valueOf(num);
    }

    public void saveVerificationCode(Long userId, String email, String verificationCode) {
        Optional<VerificationCode> existingCode = verificationCodeRepository.findByUserIdAndEmail(userId, email);

        VerificationCode codeEntity;
        if (existingCode.isPresent()) {
            codeEntity = existingCode.get();
            codeEntity.setVerificationCode(verificationCode);
        } else {
            codeEntity = new VerificationCode();
            codeEntity.setUserId(userId);
            codeEntity.setEmail(email);
            codeEntity.setVerificationCode(verificationCode);
        }
        verificationCodeRepository.save(codeEntity);
    }

    public boolean verifyVerificationCode(String email, String verificationCode) {
        VerificationCode codeEntity = verificationCodeRepository.findByEmailAndVerificationCode(email, verificationCode);
        return codeEntity != null;
    }

    @Override
    public StandardResponse findById(Long id) {
        StandardResponse standardResponse = new StandardResponse();
        if (id != null) {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                UserDTO userDTO = new UserDTO();
                userDTO.setFirstName(user.getFirstName());
                userDTO.setLastName(user.getLastName());
                userDTO.setUserAvatar(CommonFunctions.getStringImage(user.getUserAvatar()));

                KeyValueDTO<UserDTO> userDTOKeyValueDTO = new KeyValueDTO<>("userData", userDTO);

                standardResponse.setCode("200");
                standardResponse.setData(Collections.singletonList(userDTOKeyValueDTO));
            }
        } else {
            standardResponse.setCode(String.valueOf(401));
            standardResponse.setMessage("User Id Cannot Be Empty");
        }
        return standardResponse;
    }

    @Override
    public StandardResponse deleteById(Long aLong) {
        return null;
    }
}
