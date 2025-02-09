/**
 * This class represents Balance details such as cardNumber and amount.
 */
package com.zero.paymentprocessor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BalanceDto {
    private String cardNumber;
    private Double amount;
}
