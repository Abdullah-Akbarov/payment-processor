package com.zero.paymentprocessor.controller;

import com.zero.paymentprocessor.model.MessageModel;
import com.zero.paymentprocessor.model.ResponseModel;
import com.zero.paymentprocessor.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Log4j2
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    /**
     * This method handles GET requests to the /transactions/today endpoint.
     * It returns all transaction history of today for a card.
     */
    @GetMapping("/today")
    public ResponseModel getTransactionForToday(@RequestParam String cardNumber, @RequestParam int page) {
        log.info(">> getTransactionForToday: cardNumber=" + cardNumber + " page=" + page);
        return transactionService.getToday(cardNumber, page);
    }

    /**
     * This method handles GET requests to the /transactions/month endpoint.
     * It returns all transaction history of picked month for a card.
     */
    @GetMapping("/month")
    public ResponseModel getTransactionForMonth(@RequestParam int year, @RequestParam int month, @RequestParam String cardNumber, @RequestParam int page) {
        log.info(">> getTransactionForMonth: year=" + year + " month=" + month + " cardNumber=" + cardNumber + " page=" + page);
        if (year < 2022 || year > LocalDate.now().getYear()) {
            log.warn("<< getTransactionForMonth: Invalid year");
            return new ResponseModel(MessageModel.INVALID_YEAR);
        }
        if (month < 1 || month > 12) {
            log.warn("<< getTransactionForMonth: Invalid month");
            return new ResponseModel(MessageModel.INVALID_MONTH);
        }
        return transactionService.getMonth(year, month, cardNumber, page);
    }

    /**
     * This method handles GET requests to the /transactions/custom endpoint.
     * It returns custom date transaction history of picked month for a card.
     */
    @GetMapping("/custom")
    public ResponseModel getTransactionForCustom(@RequestParam String startDate, @RequestParam String endDate, @RequestParam String cardNumber, @RequestParam int page) {
        log.info(">> getTransactionForCustom: startDate=" + startDate + " endDate=" + startDate + " cardNumber=" + cardNumber + " page=" + page);
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        if (start.isAfter(end)) {
            log.warn("<< getTransactionForCustom: Invalid Date");
            return new ResponseModel(MessageModel.INVALID_DATE);

        }
        if (start.isBefore(LocalDate.of(2022, 1, 1))) {
            log.warn("<< getTransactionForCustom: Invalid Date");
            return new ResponseModel(MessageModel.INVALID_DATE);
        }
        return transactionService.getByCustom(start, end, cardNumber, page);
    }
}
