package com.zero.paymentprocessor.projection;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public interface TransactionProjection {
    Long getId();
    Double getAmount();
    String getSender();
    String getReceiver();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    Timestamp getDateTime();
}
