package com.zero.paymentprocessor.service;

import com.zero.paymentprocessor.domain.User;
import com.zero.paymentprocessor.model.ResponseModel;

public interface AuthService {
    ResponseModel login(User user);

    ResponseModel register(User user);
}
