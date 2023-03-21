package com.zero.paymentprocessor.controller;

import com.zero.paymentprocessor.domain.User;
import com.zero.paymentprocessor.dto.UserDto;
import com.zero.paymentprocessor.dto.UserLoginDto;
import com.zero.paymentprocessor.model.ResponseModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final ModelMapper mapper;

    @PostMapping("/login")
    public ResponseModel login(@Valid @RequestBody UserLoginDto userLoginDto) {
//        return userService.login(mapper.map(userLoginDto, User.class));
        return null;
    }

    @PostMapping("/register")
    ResponseModel saveUser(@Valid @RequestBody UserDto userDto) {
        mapper.typeMap(UserDto.class, User.class).addMappings(mapper -> mapper.skip(User::setId));
//        return userService.save(mapper.map(userDto, User.class));
        return null;
    }
}
