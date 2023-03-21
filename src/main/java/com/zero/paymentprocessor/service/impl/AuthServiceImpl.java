package com.zero.paymentprocessor.service.impl;

import com.zero.paymentprocessor.domain.User;
import com.zero.paymentprocessor.model.ResponseModel;
import com.zero.paymentprocessor.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public ResponseModel login(User user) {
        return null;
    }

    @Override
    public ResponseModel register(User user) {
        return null;
    }
}
