package com.zero.paymentprocessor.controller;

import com.zero.paymentprocessor.dto.UserDto;
import com.zero.paymentprocessor.dto.UserLoginDto;
import com.zero.paymentprocessor.model.ResponseModel;
import com.zero.paymentprocessor.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseModel login(@Valid @RequestBody UserLoginDto userLoginDto) {
        return authService.login(userLoginDto);
    }

    @PostMapping("/register")
    ResponseModel saveUser(@Valid @RequestBody UserDto userDto) {
        return authService.register(userDto);
    }
}
