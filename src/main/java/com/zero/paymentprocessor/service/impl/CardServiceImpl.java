package com.zero.paymentprocessor.service.impl;

import com.zero.paymentprocessor.dto.CardDto;
import com.zero.paymentprocessor.model.ResponseModel;
import com.zero.paymentprocessor.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    @Override
    public ResponseModel addCard(CardDto cardDto) {
        return null;
    }

    @Override
    public ResponseModel removeCard(String cardNumber) {
        return null;
    }

    @Override
    public ResponseModel transfer(String sender, String receiver) {
        return null;
    }
}
