/**
 * This class represents card details.
 */
package com.zero.paymentprocessor.dto;

import com.zero.paymentprocessor.domain.enums.CardCategory;
import com.zero.paymentprocessor.domain.enums.CardType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CardDto {
    @Size(min = 16, max = 16, message = "Wrong card number, card number should be 16 digits")
    @Pattern(regexp = "\\d+", message = "Card number can only contain digits")
    private String cardNumber;
    @NotNull(message = "")
    private String cardHolder;
    @NotNull(message = "expireDate cannot be empty")
    private String expireDate;
    @NotNull(message = "passCode cannot be empty")
    private String passCode;
    @NotNull(message = "CardType cannot be empty")
    private CardType cardType;
    @NotNull(message = "CardCategory cannot be empty")
    private CardCategory cardCategory;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder.toUpperCase();
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getPassCode() {
        return passCode;
    }

    public void setPassCode(String passCode) {
        this.passCode = passCode;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public CardCategory getCardCategory() {
        return cardCategory;
    }

    public void setCardCategory(CardCategory cardCategory) {
        this.cardCategory = cardCategory;
    }
}
