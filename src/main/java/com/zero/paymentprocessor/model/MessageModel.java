/**
 * This enum is used to response some messages to api.
 */

package com.zero.paymentprocessor.model;

public enum MessageModel {
    SUCCESS(200, "OK"),
    DTO_VALIDATION_ERROR(403, "DTO VALIDATION ERROR"),
    NOT_FOUND(404, "NOT FOUND"),
    CARD_NOT_FOUND(404, "CARD NOT FOUND"),
    AUTHENTICATION_FAILED(403, "AUTHENTICATION FAILED"),
    RECORD_AlREADY_EXIST(409, "RECORD ALREADY EXIST"),
    COULD_NOT_UPDATE_RECORD(409, "COULD NOT UPDATE RECORD"),
    COULD_NOT_SAVE_RECORD(409, "COULD NOT SAVE RECORD"),
    COULD_NOT_DELETE_RECORD(409, "COULD NOT DELETE RECORD"),
    INSUFFICIENT_BALANCE(410, "INSUFFICIENT BALANCE"),
    UNAUTHORIZED(411, "UNAUTHORIZED"),
    SYSTEM_ERROR(500, "SYSTEM ERROR"),
    SENDER_NOT_FOUND(412, "SENDER_NOT_FOUND");

    private final String message;
    private final int code;

    MessageModel(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
