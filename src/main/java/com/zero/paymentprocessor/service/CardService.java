package com.zero.paymentprocessor.service;

import com.zero.paymentprocessor.dto.CardDto;
import com.zero.paymentprocessor.model.ResponseModel;

public interface CardService {
    ResponseModel addCard(CardDto cardDto);

    ResponseModel removeCard(String cardNumber);

    ResponseModel transfer(String sender, String receiver);
}
