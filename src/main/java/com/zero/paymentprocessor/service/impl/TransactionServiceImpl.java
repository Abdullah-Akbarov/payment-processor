package com.zero.paymentprocessor.service.impl;

import com.zero.paymentprocessor.domain.Card;
import com.zero.paymentprocessor.domain.User;
import com.zero.paymentprocessor.model.MessageModel;
import com.zero.paymentprocessor.model.ResponseModel;
import com.zero.paymentprocessor.projection.TransactionProjection;
import com.zero.paymentprocessor.repository.CardRepository;
import com.zero.paymentprocessor.repository.TransactionRepository;
import com.zero.paymentprocessor.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;
    private final Integer pageSize = 10;

    /**
     * This method retrieves today's transaction history by given parameters.
     *
     * @param cardNumber The card number to retrieve its transactions.
     * @param page       page number of transaction.
     * @return pageable transaction history.
     */
    @Override
    public ResponseModel getToday(String cardNumber, int page) {
        log.info(">> getToday: cardNumber=" + cardNumber + " page=" + page);
        if (isAuthorized(cardNumber) == null) {
            log.warn("<< getToday: Unauthorized");
            return new ResponseModel(MessageModel.UNAUTHORIZED);
        }
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
        Page<TransactionProjection> transactionPage = transactionRepository
                .findBySenderAndDateTimeBetween(cardNumber, Timestamp.valueOf(startOfDay), Timestamp.valueOf(endOfDay), getPageable(page));
        if (!transactionPage.hasContent()) {
            log.warn("<< getToday: No content");
            return new ResponseModel(MessageModel.NO_CONTENT);
        }
        log.info("<< getToday: success");
        return new ResponseModel(MessageModel.SUCCESS, transactionPage.getContent());
    }

    /**
     * This method retrieves Transaction history for specific month of the year by given parameters.
     *
     * @param year       This parameter is used specify year of the month.
     * @param month      This parameter is used specify month of the year.
     * @param cardNumber The card number to retrieve its transactions.
     * @param page       page number of transaction.
     * @return pageable transaction history.
     */
    @Override
    public ResponseModel getMonth(int year, int month, String cardNumber, int page) {
        log.info(">> getMonth: year=" + year + " month=" + month + " cardNumber=" + cardNumber + " page=" + page);
        if (isAuthorized(cardNumber) == null) {
            log.warn("<< getMonth: Unauthorized");
            return new ResponseModel(MessageModel.UNAUTHORIZED);
        }
        LocalDateTime startOfMonth = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);
        Page<TransactionProjection> transactionPage = transactionRepository
                .findBySenderAndDateTimeBetween(cardNumber, Timestamp.valueOf(startOfMonth), Timestamp.valueOf(endOfMonth), getPageable(page));
        if (!transactionPage.hasContent()) {
            log.warn("<< getMonth: No content");
            return new ResponseModel(MessageModel.NO_CONTENT);
        }
        log.info("<< getMonth: success");
        return new ResponseModel(MessageModel.SUCCESS, transactionPage.getContent());
    }

    /**
     * This method retrieves transaction history by custom length of days.
     *
     * @param startDate  This parameter is used to specify start date.
     * @param endDate    This parameter is used to specify end date.
     * @param cardNumber The card number to retrieve its transactions.
     * @param page       page number of transaction.
     * @return pageable transaction history.
     */
    @Override
    public ResponseModel getByCustom(LocalDate startDate, LocalDate endDate, String cardNumber, int page) {
        log.info(">> getByCustom: startDate=" + startDate + " endDate=" + startDate + " cardNumber=" + cardNumber + " page=" + page);
        if (isAuthorized(cardNumber) == null) {
            log.warn("<< getByCustom: Unauthorized");
            return new ResponseModel(MessageModel.UNAUTHORIZED);
        }
        LocalDateTime startOfDate = startDate.atStartOfDay();
        LocalDateTime startOfDate1 = endDate.atStartOfDay();
        LocalDateTime endOfDate = startOfDate1.plusDays(1).minusNanos(1);
        Page<TransactionProjection> transactionPage = transactionRepository
                .findBySenderAndDateTimeBetween(cardNumber, Timestamp.valueOf(startOfDate), Timestamp.valueOf(endOfDate), getPageable(page));
        if (!transactionPage.hasContent()) {
            log.warn("<< getByCustom: No content");
            return new ResponseModel(MessageModel.NO_CONTENT);
        }
        log.info("<< getByCustom: success");
        return new ResponseModel(MessageModel.SUCCESS, transactionPage.getContent());
    }

    /**
     * This method creates pageable.
     *
     * @param page This parameter is used to specify page.
     * @return Pageable.
     */
    private Pageable getPageable(int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "dateTime");
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        return pageable;
    }

    /**
     * This method is for authorizing user for some action.
     *
     * @param cardNumber
     * @return User entity.
     */
    private User isAuthorized(String cardNumber) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Optional<Card> byCardNumber = cardRepository.findByCardNumber(cardNumber);
        if (!byCardNumber.isPresent()) {
            return null;
        }
        return byCardNumber.get().getUser().getId() == user.getId() ? user : null;
    }
}
