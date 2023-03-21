package com.zero.paymentprocessor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.YearMonth;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {
    @Size(min = 16, max = 16, message = "Wrong card number, card number should be 16 digits")
    @Pattern(regexp = "\\d+", message = "Card number can only contain digits")
    private String cardNumber;
    @NotEmpty(message = "yearMonth cannot be empty")
    private YearMonth dateTime;
    @NotEmpty(message = "passCode cannot be empty")
    private String passCode;
}
