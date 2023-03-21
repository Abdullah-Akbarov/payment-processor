package com.zero.paymentprocessor.bank;

import com.zero.paymentprocessor.domain.enums.CardCategory;
import com.zero.paymentprocessor.domain.enums.CardType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "bank_card")
@Getter
@Setter
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(unique = true)
    private String cardNumber;
    @Column(nullable = false)
    private String CardHolder;
    @Enumerated(EnumType.STRING)
    private CardType cardType;
    @Enumerated(EnumType.STRING)
    private CardCategory cardCategory;
    @Column(nullable = false)
    private String expireDate;
    @Column(nullable = false)
    private String passCode;
    @Column(nullable = false)
    private Double balance;
}
