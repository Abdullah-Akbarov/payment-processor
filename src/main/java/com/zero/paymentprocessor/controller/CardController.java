package com.zero.paymentprocessor.controller;

import com.zero.paymentprocessor.dto.CardDto;
import com.zero.paymentprocessor.dto.TransactionDto;
import com.zero.paymentprocessor.model.ResponseModel;
import com.zero.paymentprocessor.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping
    public ResponseModel saveCard(@Valid @RequestBody CardDto cardDto) {
        return cardService.addCard(cardDto);
    }

    @DeleteMapping
    public ResponseModel removeCard(@RequestParam String cardNumber) {
        return cardService.removeCard(cardNumber);
    }

    @PutMapping
    public ResponseModel transfer(@Valid @RequestBody TransactionDto transactionDto) {
        return cardService.transfer(transactionDto);
    }

    @GetMapping("/balance")
    public ResponseModel getBalance(@RequestParam String cardNumber) {
        return cardService.balance(cardNumber);
    }

}
