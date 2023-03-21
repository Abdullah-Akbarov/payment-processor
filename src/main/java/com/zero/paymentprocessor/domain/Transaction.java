package com.zero.paymentprocessor.domain;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Double amount;
    private String sender;
    private String receiver;
    private Timestamp dateTime;

}
