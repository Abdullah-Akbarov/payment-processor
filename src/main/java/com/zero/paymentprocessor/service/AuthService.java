package com.zero.paymentprocessor.service;

import com.zero.paymentprocessor.dto.UserDto;
import com.zero.paymentprocessor.dto.UserLoginDto;
import com.zero.paymentprocessor.model.ResponseModel;

public interface AuthService {
    ResponseModel login(UserLoginDto userLoginDto);

    ResponseModel register(UserDto userDto);
}
