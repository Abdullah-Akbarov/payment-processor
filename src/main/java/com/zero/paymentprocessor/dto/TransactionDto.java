/**
 * This class represents Transaction details;
 */

package com.zero.paymentprocessor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    @DecimalMin(value = "1000", message = "minimum amount of money is 1000")
    private Double amount;
    @Size(min = 16, max = 16, message = "Wrong card number, card number should be 16 digits")
    @Pattern(regexp = "\\d+", message = "Card number can only contain digits")
    private String sender;
    @Size(min = 16, max = 16, message = "Wrong card number, card number should be 16 digits")
    @Pattern(regexp = "\\d+", message = "Card number can only contain digits")
    private String receiver;
    private Timestamp dateTime;

}
