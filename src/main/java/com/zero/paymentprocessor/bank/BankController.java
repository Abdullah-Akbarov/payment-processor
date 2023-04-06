package com.zero.paymentprocessor.bank;

import com.zero.paymentprocessor.dto.BalanceDto;
import com.zero.paymentprocessor.dto.CardDto;
import com.zero.paymentprocessor.model.ResponseModel;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
public class BankController {
    private static final Logger logger = LogManager.getLogger(BankController.class);
    private final CardService cardService;

    /**
     * This method handles POST request to the /bank/validate endpoint.
     * It checks if card is valid.
     *
     * @param cardDto The Card information to validate the card.
     */
    @PostMapping("/validate")
    public ResponseModel validate(@Valid @RequestBody CardDto cardDto) {
        logger.info(">> validate: " + cardDto);
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
        logger.info(">> getBalance: cardNumber=" + cardNumber);
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
        logger.info(">> checkCard: cardNumber=" + cardNumber);
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
        logger.info(">> saveCard cardNumber = " + cardSaveDto);
        return cardService.saveCard(cardSaveDto);
    }

    /**
     * This method handles PUT requests to the /bank/update endpoint.
     *
     * @param balanceDto The balance information to update card balance.
     */
    @PutMapping("/update")
    public ResponseModel updateBalance(@RequestBody BalanceDto balanceDto) {
        logger.info(">> updateBalance: cardNumber=" + balanceDto);
        return cardService.updateBalance(balanceDto);
    }
}
