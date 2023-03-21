package com.zero.paymentprocessor.domain;

import com.zero.paymentprocessor.domain.enums.CardCategory;
import com.zero.paymentprocessor.domain.enums.CardType;

import javax.persistence.*;

@Entity(name = "card")
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
    @ManyToOne
    private User user;
}
