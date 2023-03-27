package com.zero.paymentprocessor.service.impl;

import com.zero.paymentprocessor.domain.Card;
import com.zero.paymentprocessor.domain.Transaction;
import com.zero.paymentprocessor.domain.User;
import com.zero.paymentprocessor.dto.BalanceDto;
import com.zero.paymentprocessor.dto.CardDto;
import com.zero.paymentprocessor.dto.TransactionDto;
import com.zero.paymentprocessor.model.MessageModel;
import com.zero.paymentprocessor.model.ResponseModel;
import com.zero.paymentprocessor.repository.CardRepository;
import com.zero.paymentprocessor.repository.TransactionRepository;
import com.zero.paymentprocessor.service.CardService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final RestTemplate restTemplate;
    private final CardRepository cardRepository;
    private final ModelMapper mapper;
    private final HttpHeaders headers;
    private final TransactionRepository transactionRepository;

    @Override
    public ResponseModel addCard(CardDto cardDto) {
        if (cardRepository.findByCardNumber(cardDto.getCardNumber()).isPresent()) {
            return new ResponseModel(MessageModel.RECORD_AlREADY_EXIST);
        }
        encrypt(cardDto);
        String url = "http://localhost:8080/bank/validate";
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CardDto> entity = new HttpEntity<>(cardDto, headers);
        ResponseEntity<ResponseModel> response = restTemplate.postForEntity(url, entity, ResponseModel.class);
        if (response.getBody().status == 200) {
            Card card = mapper.map(cardDto, Card.class);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            card.setUser((User) authentication.getPrincipal());
            Card save = cardRepository.save(card);
            if (save.getId() != null) {
                return new ResponseModel(MessageModel.SUCCESS);
            }
            return new ResponseModel(MessageModel.COULD_NOT_SAVE_RECORD);
        }
        return new ResponseModel(MessageModel.CARD_NOT_FOUND);
    }

    @Override
    public ResponseModel removeCard(String cardNumber) {
        Optional<Card> byCardNumber = cardRepository.findByCardNumber(cardNumber);
        if (byCardNumber.isPresent()) {
            if (cardRepository.deleteCardByCardNumber(cardNumber) == 1) {
                return new ResponseModel(MessageModel.SUCCESS);
            }
            return new ResponseModel(MessageModel.COULD_NOT_DELETE_RECORD);
        }
        return new ResponseModel(MessageModel.CARD_NOT_FOUND);
    }

    @Override
    public ResponseModel transfer(TransactionDto transactionDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Transaction map = mapper.map(transactionDto, Transaction.class);
        User user = (User) authentication.getPrincipal();
        Optional<Card> byCardNumber = cardRepository.findByCardNumber(transactionDto.getSender());
        if (!byCardNumber.isPresent()) {
            return new ResponseModel(MessageModel.SENDER_NOT_FOUND);
        }
        if (byCardNumber.get().getUser().getId() != user.getId()) {
            return new ResponseModel(MessageModel.UNAUTHORIZED);
        }
        if (!checkCard(transactionDto.getReceiver()) || !checkCard(transactionDto.getSender())) {
            return new ResponseModel(MessageModel.NOT_FOUND);
        }
        Double balance = getBalance(transactionDto.getSender());
        if (balance != null) {
            if (balance > transactionDto.getAmount()) {
                boolean sender = updateBalance(new BalanceDto(transactionDto.getSender(), -1 * transactionDto.getAmount()));
                boolean receiver = updateBalance(new BalanceDto(transactionDto.getReceiver(), transactionDto.getAmount()));
                if (sender && receiver) {
                    transactionDto.setDateTime(new Timestamp(System.currentTimeMillis()));
                    map.setUser(user);
                    transactionRepository.save(map);
                }
                return new ResponseModel(MessageModel.SUCCESS);
            }
            return new ResponseModel(MessageModel.INSUFFICIENT_BALANCE);
        }
        return new ResponseModel(MessageModel.SYSTEM_ERROR);
    }

    @Override
    public ResponseModel balance(String cardNumber) {
        Double balance = getBalance(cardNumber);
        if (balance != null) {
            return new ResponseModel(MessageModel.SUCCESS, balance);
        }
        return new ResponseModel(MessageModel.CARD_NOT_FOUND);
    }

    private Double getBalance(String cardNumber) {
        ResponseModel balance = getRequests(cardNumber, "balance");
        if (balance.status == 200) {
            return (Double) balance.data;
        }
        return null;
    }

    private boolean checkCard(String cardNumber) {
        ResponseModel check = getRequests(cardNumber, "check");
        return check.status == 200;
    }

    private void encrypt(CardDto cardDto) {
        String password = "myBankSecretKey";
        String salt = "5c0744940b5c369b";
        TextEncryptor text = Encryptors.text(password, salt);
        cardDto.setPassCode(text.encrypt(cardDto.getPassCode()));
    }

    private ResponseModel getRequests(String cardNumber, String path) {
        String url = "http://localhost:8080/bank/" + path;
        headers.set("Accept", "application/json");
        Map<String, String> params = new HashMap<String, String>();
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
}
