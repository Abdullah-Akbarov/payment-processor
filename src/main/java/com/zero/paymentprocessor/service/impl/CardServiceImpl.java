package com.zero.paymentprocessor.service.impl;

import com.zero.paymentprocessor.domain.Card;
import com.zero.paymentprocessor.domain.Transaction;
import com.zero.paymentprocessor.domain.User;
import com.zero.paymentprocessor.dto.BalanceDto;
import com.zero.paymentprocessor.dto.CardDto;
import com.zero.paymentprocessor.dto.TransactionDto;
import com.zero.paymentprocessor.model.MessageModel;
import com.zero.paymentprocessor.model.ResponseModel;
import com.zero.paymentprocessor.projection.CardProjection;
import com.zero.paymentprocessor.repository.CardRepository;
import com.zero.paymentprocessor.repository.TransactionRepository;
import com.zero.paymentprocessor.service.CardService;
import com.zero.paymentprocessor.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final RestTemplate restTemplate;
    private final CardRepository cardRepository;
    private final ModelMapper mapper;
    private final HttpHeaders headers;
    private final TransactionRepository transactionRepository;
    private final Utils utils;
    @Value("${secret.key}")
    private String secretKey;

    /**
     * This method saves the provided card entity into the database.
     *
     * @param cardDto The card dto to be saved and mapped as entity.
     */
    @Override
    public ResponseModel addCard(CardDto cardDto) {
        log.info(">> addCard: cardNumber=" + cardDto.getCardNumber() + " cardHolder=" + cardDto.getCardHolder() +
                "expireDate=" + cardDto.getExpireDate());
        if (cardRepository.existsByCardNumber(cardDto.getCardNumber())) {
            log.warn("<< addCard: Record already exist");
            return new ResponseModel(MessageModel.RECORD_AlREADY_EXIST);
        }
        encrypt(cardDto);
        if (validateCard(cardDto)) {
            Card card = mapper.map(cardDto, Card.class);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            card.setUser((User) authentication.getPrincipal());
            Card save = cardRepository.save(card);
            log.info("<< addCard: Success");
            return new ResponseModel(MessageModel.SUCCESS);
        }
        log.warn("<< addCard: Card not found");
        return new ResponseModel(MessageModel.CARD_NOT_FOUND);
    }

    /**
     * This method removes the provided card entity from database.
     *
     * @param cardNumber The cardNumber for removing.
     */
    @Override
    public ResponseModel removeCard(String cardNumber) {
        log.info(">> removeCard: cardNumber=" + cardNumber);
        if (utils.isAuthorized(cardNumber) == null) {
            log.warn("<< removeCard: Unauthorized");
            return new ResponseModel(MessageModel.UNAUTHORIZED);
        }
        if (cardRepository.existsByCardNumber(cardNumber)) {
            if (cardRepository.deleteCardByCardNumber(cardNumber) == 1) {
                log.info("<< removeCard: Success");
                return new ResponseModel(MessageModel.SUCCESS);
            }
            log.warn("<< removeCard: Couldn't delete record");
            return new ResponseModel(MessageModel.COULD_NOT_DELETE_RECORD);
        }
        log.warn("<< removeCard: Card not found");
        return new ResponseModel(MessageModel.CARD_NOT_FOUND);
    }

    /**
     * This method is used to transfer money between two cards.
     *
     * @param transactionDto The transaction details.
     */
    @Override
    public ResponseModel transfer(TransactionDto transactionDto) {
        log.info(">> transfer: " + transactionDto);
        User user = utils.isAuthorized(transactionDto.getSender());
        if (user == null) {
            log.warn("<< transfer: Unauthorized");
            return new ResponseModel(MessageModel.UNAUTHORIZED);
        }
        if (!checkCard(transactionDto.getReceiver()) || !checkCard(transactionDto.getSender())) {
            log.warn("<< transfer: Not found");
            return new ResponseModel(MessageModel.NOT_FOUND);
        }
        Double balance = getBalance(transactionDto.getSender());
        if (balance != null) {
            if (balance >= transactionDto.getAmount()) {
                Transaction map = mapper.map(transactionDto, Transaction.class);
                boolean sender = updateBalance(new BalanceDto(transactionDto.getSender(), -1 * transactionDto.getAmount()));
                boolean receiver = updateBalance(new BalanceDto(transactionDto.getReceiver(), transactionDto.getAmount()));
                if (sender && receiver) {
                    map.setDateTime(new Timestamp(System.currentTimeMillis()));
                    map.setUser(user);
                    transactionRepository.save(map);
                }
                log.info("<< transfer: Success");
                return new ResponseModel(MessageModel.SUCCESS);
            }
            log.warn("<< transfer: Insufficient balance");
            return new ResponseModel(MessageModel.INSUFFICIENT_BALANCE);
        }
        log.warn("<< transfer: System Error");
        return new ResponseModel(MessageModel.SYSTEM_ERROR);
    }

    @Override
    public ResponseModel balance(String cardNumber) {
        if (utils.isAuthorized(cardNumber) == null) {
            log.warn("<< balance: Unauthorized");
            return new ResponseModel(MessageModel.UNAUTHORIZED);
        }
        Double balance = getBalance(cardNumber);
        if (balance != null) {
            log.info("<< balance: Success");
            return new ResponseModel(MessageModel.SUCCESS, balance);
        }
        log.warn("<< balance: Card not found");
        return new ResponseModel(MessageModel.CARD_NOT_FOUND);
    }

    /**
     * This method retrieves all card from database.
     *
     * @return list of cards;
     */
    @Override
    public ResponseModel getAllCards() {
        log.info(">> getAllCards");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<CardProjection> byUser = cardRepository.findCardByUser(user);
        if (!byUser.isEmpty()) {
            log.info("<< getAllCards: Success");
            return new ResponseModel(MessageModel.SUCCESS, byUser);
        }
        log.warn("<< getAllCards: Card not found");
        return new ResponseModel(MessageModel.CARD_NOT_FOUND);
    }

    /**
     * This method retrieves card balance from bank api.
     *
     * @param cardNumber The card number to find card.
     * @return balance of the card;
     */
    private Double getBalance(String cardNumber) {
        ResponseModel balance = getRequests(cardNumber, "balance");
        if (balance.status == 200) {
            return (Double) balance.data;
        }
        return null;
    }

    /**
     * This method sends request to bank api if card exists.
     *
     * @param cardNumber The card number to find card.
     */
    private boolean checkCard(String cardNumber) {
        ResponseModel check = getRequests(cardNumber, "check");
        return check.status == 200;
    }

    /**
     * This method is used encrypt card Passcode.
     */
    private void encrypt(CardDto cardDto) {
        String salt = "5c0744940b5c369b";
        TextEncryptor text = Encryptors.text(secretKey, salt);
        cardDto.setPassCode(text.encrypt(cardDto.getPassCode()));
    }

    /**
     * This method is used to send GET requests to bank api.
     *
     * @param cardNumber card number for sending in GET request.
     * @param path       path of the api.
     */
    private ResponseModel getRequests(String cardNumber, String path) {
        String url = "http://localhost:8080/bank/" + path;
        headers.set("Accept", "application/json");
        Map<String, String> params = new HashMap<>();
        params.put("cardNumber", cardNumber);

        HttpEntity entity = new HttpEntity(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("cardNumber", params.get("cardNumber"));

        ResponseEntity<ResponseModel> exchange = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                ResponseModel.class);
        return exchange.getBody();
    }

    /**
     * This method is used to send balance update request to bank api.
     *
     * @param balanceDto The balance details in order to update card balance.
     * @return boolean according to balance updated.
     */
    private boolean updateBalance(BalanceDto balanceDto) {
        String url = "http://localhost:8080/bank/update";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<BalanceDto> requestEntity = new HttpEntity<>(balanceDto, headers);

        ResponseEntity<ResponseModel> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                requestEntity,
                ResponseModel.class);
        return response.getBody().status == 200;
    }

    /**
     * This method sends validation request to bank api.
     *
     * @param cardDto The card details in order to validate.
     * @return boolean according to validity of the card.
     */
    private boolean validateCard(CardDto cardDto) {
        String url = "http://localhost:8080/bank/validate";
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CardDto> entity = new HttpEntity<>(cardDto, headers);
        ResponseEntity<ResponseModel> response = restTemplate.postForEntity(url, entity, ResponseModel.class);
        return response.getBody().status == 200;
    }
}
