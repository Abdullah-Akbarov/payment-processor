package com.zero.paymentprocessor.dto;

import lombok.Data;

@Data
public class UpdateBalanceDto {
    private String cardNumber;
    private Double amount;
}
