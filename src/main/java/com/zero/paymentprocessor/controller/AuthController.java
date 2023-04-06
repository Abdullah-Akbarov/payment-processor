package com.zero.paymentprocessor.controller;

import com.zero.paymentprocessor.dto.UserDto;
import com.zero.paymentprocessor.dto.UserLoginDto;
import com.zero.paymentprocessor.model.ResponseModel;
import com.zero.paymentprocessor.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private static final Logger logger = LogManager.getLogger(AuthController.class);

    /**
     * This method handles GET requests to the /auth/login endpoint.
     * It checks if user exist and authorized to log in this account.
     *
     * @param userLoginDto User login information.
     * @return jwt token.
     */
    @GetMapping("/login")
    public ResponseModel login(@Valid @RequestBody UserLoginDto userLoginDto) {
        logger.info(">> login: username=" + userLoginDto.getUsername());
        return authService.login(userLoginDto);
    }

    /**
     * This method handles POST requests to the /auth/register endpoint.
     * It creates a new User with the provided information.
     *
     * @param userDto The user information.
     * @return jwt token.
     */
    @PostMapping("/register")
    ResponseModel saveUser(@Valid @RequestBody UserDto userDto) {
        logger.info(">> saveUser: username=" + userDto.getUsername() + " phoneNumber=" + userDto.getPhoneNumber());
        return authService.register(userDto);
    }
}
