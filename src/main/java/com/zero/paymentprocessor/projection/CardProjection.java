package com.zero.paymentprocessor.projection;

import com.zero.paymentprocessor.domain.enums.CardCategory;
import com.zero.paymentprocessor.domain.enums.CardType;

public interface CardProjection {
    Long getId();

    String getCardNumber();

    String getCardHolder();

    CardType getCardType();

    CardCategory getCardCategory();

    String getExpireDate();
}
