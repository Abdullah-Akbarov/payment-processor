package com.zero.paymentprocessor.bank;

import com.zero.paymentprocessor.dto.CardDto;
import com.zero.paymentprocessor.dto.UpdateBalanceDto;
import com.zero.paymentprocessor.model.ResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
public class BankController {
    private final CardService cardService;
    @PostMapping("/validate")
    public ResponseModel validate(@Valid @RequestBody CardDto cardDto) {
        return cardService.validateCard(cardDto);
    }

    @GetMapping("/balance")
    public ResponseModel getBalance(@RequestParam String cardNumber) {
        return cardService.getBalance(cardNumber);
    }

    @GetMapping("/check")
    public ResponseModel checkCard(@RequestParam String cardNumber) {
        return cardService.getCard(cardNumber);
    }

    @PostMapping
    public ResponseModel saveCard(@Valid @RequestBody CardSaveDto cardSaveDto) {
        return cardService.saveCard(cardSaveDto);
    }

    @PutMapping
    public ResponseModel updateBalance(@RequestBody UpdateBalanceDto balanceDto) {
        return cardService.updateBalance(balanceDto);
    }

}
