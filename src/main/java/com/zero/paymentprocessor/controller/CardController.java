package com.zero.paymentprocessor.controller;

import com.zero.paymentprocessor.dto.CardDto;
import com.zero.paymentprocessor.dto.TransactionDto;
import com.zero.paymentprocessor.model.MessageModel;
import com.zero.paymentprocessor.model.ResponseModel;
import com.zero.paymentprocessor.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Log4j2
@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    /**
     * This method handles POST requests to the /cards endpoint.
     * It creates a new card in payment processor with the provided information.
     *
     * @param cardDto The Card information to save the card.
     */
    @PostMapping
    public ResponseModel saveCard(@Valid @RequestBody CardDto cardDto) {
        try {
            log.info(">> saveCard: cardNumber=" + cardDto.getCardNumber() + " cardHolder=" + cardDto.getCardHolder() +
                    "expireDate=" + cardDto.getExpireDate());
            return cardService.addCard(cardDto);
        } catch (Exception e) {
            log.warn("<< saveCard: Couldn't save record");
            return new ResponseModel(MessageModel.COULD_NOT_SAVE_RECORD, e);
        }
    }

    /**
     * This method handles DELETE requests to the /cards/remove endpoint.
     * It removes card from payment processor.
     *
     * @param cardNumber The Card number to remove.
     */
    @DeleteMapping("/remove")
    public ResponseModel removeCard(@RequestParam String cardNumber) {
        log.info(">> removeCard: cardNumber=" + cardNumber);
        return cardService.removeCard(cardNumber);
    }

    /**
     * This method handles PUT requests to the /cards/transfer endpoint.
     * It transfers money from one to another.
     *
     * @param transactionDto The transaction details.
     */
    @PutMapping("/transfer")
    public ResponseModel transfer(@Valid @RequestBody TransactionDto transactionDto) {
        log.info(">> transfer: " + transactionDto);
        return cardService.transfer(transactionDto);
    }

    /**
     * This method handles GET request to the /cards/balance endpoint.
     * It retrieves card balance with the provided information.
     *
     * @param cardNumber The Card number to get balance.
     */
    @GetMapping("/balance")
    public ResponseModel getBalance(@RequestParam String cardNumber) {
        log.info(">> getBalance: cardNumber=" + cardNumber);
        return cardService.balance(cardNumber);
    }

}
