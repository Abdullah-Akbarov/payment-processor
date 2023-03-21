package com.zero.paymentprocessor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Double amount;
    private String sender;
    private String receiver;
    private Timestamp dateTime;

}
