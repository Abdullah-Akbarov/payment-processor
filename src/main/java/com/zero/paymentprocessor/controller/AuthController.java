package com.zero.paymentprocessor.controller;

import com.zero.paymentprocessor.dto.UserDto;
import com.zero.paymentprocessor.dto.UserLoginDto;
import com.zero.paymentprocessor.model.ResponseModel;
import com.zero.paymentprocessor.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Log4j2
public class AuthController {
    private final AuthService authService;

    /**
     * This method handles GET requests to the /auth/login endpoint.
     * It checks if user exist and authorized to log in this account.
     *
     * @param userLoginDto User login information.
     * @return jwt token.
     */
    @GetMapping("/login")
    public ResponseModel login(@Valid @RequestBody UserLoginDto userLoginDto) {
        log.info(">> login: username=" + userLoginDto.getUsername());
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
        log.info(">> saveUser: username=" + userDto.getUsername() + " phoneNumber=" + userDto.getPhoneNumber());
        return authService.register(userDto);
    }
}
