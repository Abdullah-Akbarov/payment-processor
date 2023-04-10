/**
 * This class is used to handle global exceptions.
 */

package com.zero.paymentprocessor.handler;

import com.zero.paymentprocessor.model.MessageModel;
import com.zero.paymentprocessor.model.ResponseModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseModel> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.warn("dto validation error  " + errors);
        return new ResponseEntity<>(new ResponseModel(MessageModel.DTO_VALIDATION_ERROR, errors), HttpStatus.FORBIDDEN);
    }
}