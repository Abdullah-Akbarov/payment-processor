package com.zero.paymentprocessor.bank;

import com.zero.paymentprocessor.domain.enums.CardCategory;
import com.zero.paymentprocessor.domain.enums.CardType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.YearMonth;

@AllArgsConstructor
@NoArgsConstructor
public class CardSaveDto {
    @Size(min = 16, max = 16, message = "Wrong card number, card number should be 16 digits")
    @Pattern(regexp = "\\d+", message = "Card number can only contain digits")
    private String cardNumber;
    private String CardHolder;
    @NotEmpty(message = "CardType cannot be empty")
    private CardType cardType;
    @NotEmpty(message = "CardCategory cannot be empty")
    private CardCategory cardCategory;
    private YearMonth expireDate;
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

    public YearMonth getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(YearMonth expireDate) {
        this.expireDate = expireDate;
    }

    public String getPassCode() {
        return passCode;
    }

    public void setPassCode(String passCode) {
        this.passCode = passCode;
    }
}
