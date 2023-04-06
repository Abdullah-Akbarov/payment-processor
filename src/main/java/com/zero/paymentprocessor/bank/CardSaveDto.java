/**
 * This class is used to transfer card details from user.
 */

package com.zero.paymentprocessor.bank;

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
public class CardSaveDto {
    @Size(min = 16, max = 16, message = "Wrong card number, card number should be 16 digits")
    @Pattern(regexp = "\\d+", message = "Card number can only contain digits")
    private String cardNumber;
    @NotNull(message = "CardHolder cannot be empty")
    private String CardHolder;
    @NotNull(message = "CardType cannot be empty")
    private CardType cardType;
    @NotNull(message = "CardCategory cannot be empty")
    private CardCategory cardCategory;
    @NotNull(message = "expire date cannot be empty")
    private String expireDate;
    @Size(min = 4, max = 4, message = "passcode should be 4 digits long")
    private String passCode;
    private Double balance;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return CardHolder;
    }

    public void setCardHolder(String cardHolder) {
        CardHolder = cardHolder.toUpperCase();
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


    public String getPassCode() {
        return passCode;
    }

    public void setPassCode(String passCode) {
        this.passCode = passCode;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
}
