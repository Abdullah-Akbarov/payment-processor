package com.zero.paymentprocessor.service.impl;

import com.zero.paymentprocessor.domain.Card;
import com.zero.paymentprocessor.dto.CardDto;
import com.zero.paymentprocessor.model.MessageModel;
import com.zero.paymentprocessor.model.ResponseModel;
import com.zero.paymentprocessor.repository.CardRepository;
import com.zero.paymentprocessor.service.CardService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

    @Override
    public ResponseModel addCard(CardDto cardDto) {
        encrypt(cardDto);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CardDto> entity = new HttpEntity<>(cardDto, headers);
        ResponseEntity<ResponseModel> response = restTemplate.postForEntity("http://localhost:8080/bank/validate", entity, ResponseModel.class);
        if (response.getBody().status == 200) {
            Card card = mapper.map(cardDto, Card.class);
            // Todo get current authorized user and set;
            return new ResponseModel();
        }
        return new ResponseModel(MessageModel.NOT_FOUND);
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
        return new ResponseModel(MessageModel.NOT_FOUND);
    }

    @Override
    public ResponseModel transfer(String sender, String receiver) {
        return null;
    }

    @Override
    public ResponseModel balance(String cardNumber) {
        headers.set("Accept", "application/json");
        Map<String, String> params = new HashMap<String, String>();
        params.put("cardNumber", cardNumber);

        HttpEntity entity = new HttpEntity(headers);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/bank/balance")
                .queryParam("cardNumber", params.get("cardNumber"));

        ResponseEntity<ResponseModel> exchange = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                ResponseModel.class);
        if (exchange.getBody().status == 200) {
            return new ResponseModel(MessageModel.SUCCESS, exchange.getBody().data);
        }
        return new ResponseModel(MessageModel.NOT_FOUND);
    }

    private void encrypt(CardDto cardDto) {
        String password = "myBankSecretKey";
        String salt = "5c0744940b5c369b";
        TextEncryptor text = Encryptors.text(password, salt);
        cardDto.setPassCode(text.encrypt(cardDto.getPassCode()));
    }
}
