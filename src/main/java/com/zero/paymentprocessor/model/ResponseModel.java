package com.zero.paymentprocessor.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor
@Component
/**
 * Custom response model
 */
public class ResponseModel {
    public Integer status;
    public String message;
    public Object data;

    public ResponseModel(MessageModel messageStatus, Object data) {
        this.status = messageStatus.getCode();
        this.message = messageStatus.getMessage();
        this.data = data;
    }

    public ResponseModel(MessageModel messageStatus) {
        this.status = messageStatus.getCode();
        this.message = messageStatus.getMessage();
    }

    public ResponseModel(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseModel(Integer status, Object Data) {
        this.status = status;
        this.data = data;
    }
}
