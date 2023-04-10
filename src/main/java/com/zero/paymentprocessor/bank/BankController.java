package com.zero.paymentprocessor.bank;

import com.zero.paymentprocessor.dto.BalanceDto;
import com.zero.paymentprocessor.dto.CardDto;
import com.zero.paymentprocessor.model.MessageModel;
import com.zero.paymentprocessor.model.ResponseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Log4j2
@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
public class BankController {
    private final CardService cardService;

    /**
     * This method handles POST request to the /bank/validate endpoint.
     * It checks if card is valid.
     *
     * @param cardDto The Card information to validate the card.
     */
    @PostMapping("/validate")
    public ResponseModel validate(@Valid @RequestBody CardDto cardDto) {
        log.info(">> validate: " + cardDto);
        return cardService.validateCard(cardDto);
    }

    /**
     * This method handles GET requests to the /bank/balance endpoint.
     * It gets balance of card.
     *
     * @param cardNumber The card number getting balance;
     * @return Balance of the card.
     */
    @GetMapping("/balance")
    public ResponseModel getBalance(@RequestParam String cardNumber) {
        log.info(">> getBalance: cardNumber=" + cardNumber);
        return cardService.getBalance(cardNumber);
    }

    /**
     * This method handles GET requests to the /bank/check endpoint.
     * It checks if card is exist.
     *
     * @param cardNumber The card number for checking card.
     */
    @GetMapping("/check")
    public ResponseModel checkCard(@RequestParam String cardNumber) {
        log.info(">> checkCard: cardNumber=" + cardNumber);
        return cardService.getCard(cardNumber);
    }

    /**
     * This method handles POST requests to the /bank endpoint.
     * It creates a new card with the provided information.
     *
     * @param cardSaveDto The card information to be used for creating the new card.
     */
    @PostMapping
    public ResponseModel saveCard(@Valid @RequestBody CardSaveDto cardSaveDto) {
        try {
            log.info(">> saveCard " + cardSaveDto);
            return cardService.saveCard(cardSaveDto);
        } catch (Exception e) {
            log.warn("<< saveCard: Couldn't save record");
            return new ResponseModel(MessageModel.COULD_NOT_SAVE_RECORD, e);
        }
    }

    /**
     * This method handles PUT requests to the /bank/update endpoint.
     *
     * @param balanceDto The balance information to update card balance.
     */
    @PutMapping("/update")
    public ResponseModel updateBalance(@RequestBody BalanceDto balanceDto) {
        log.info(">> updateBalance: cardNumber=" + balanceDto);
        return cardService.updateBalance(balanceDto);
    }
}
