package com.zero.paymentprocessor.service;

import com.zero.paymentprocessor.model.ResponseModel;

import java.time.LocalDate;

public interface TransactionService {
    ResponseModel getToday(String cardNumber, int page);

    ResponseModel getMonth(int year, int month, String cardNumber, int page);

    ResponseModel getByCustom(LocalDate startDate, LocalDate endDate, String cardNumber, int page);
}
