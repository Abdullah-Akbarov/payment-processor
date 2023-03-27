package com.zero.paymentprocessor.bank;

import com.zero.paymentprocessor.dto.BalanceDto;
import com.zero.paymentprocessor.dto.CardDto;
import com.zero.paymentprocessor.model.MessageModel;
import com.zero.paymentprocessor.model.ResponseModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {
    private final PasswordEncoder encoder;
    private final CardRepository cardRepository;
    private final ModelMapper mapper;

    public ResponseModel getCard(String cardNumber) {
        Optional<Card> byCardNumber = cardRepository.findByCardNumber(cardNumber);
        if (byCardNumber.isPresent()) {
            return new ResponseModel(MessageModel.SUCCESS);
        }
        return new ResponseModel(MessageModel.NOT_FOUND);
    }

    public ResponseModel getBalance(String cardNumber) {
        Optional<Card> byCardNumber = cardRepository.findByCardNumber(cardNumber);
        return byCardNumber.map(card -> new ResponseModel(MessageModel.SUCCESS, card.getBalance())).orElseGet(() -> new ResponseModel(MessageModel.NOT_FOUND));
    }

    public ResponseModel validateCard(CardDto cardDto) {
        Optional<Card> byCardNumber = cardRepository.findByCardNumber(cardDto.getCardNumber());
        if (byCardNumber.isPresent()) {
            String decrypt = decrypt(cardDto.getPassCode());
            if (encoder.matches(decrypt, byCardNumber.get().getPassCode()) && cardDto.getExpireDate().equals(byCardNumber.get().getExpireDate())) {
                return new ResponseModel(MessageModel.SUCCESS);
            }
            return new ResponseModel(MessageModel.AUTHENTICATION_FAILED);
        }
        return new ResponseModel(MessageModel.NOT_FOUND);
    }

    public ResponseModel saveCard(CardSaveDto cardSaveDto) {
        if (cardRepository.findByCardNumber(cardSaveDto.getCardNumber()).isPresent()) {
            return new ResponseModel(MessageModel.RECORD_AlREADY_EXIST);
        }
        Card card = mapper.map(cardSaveDto, Card.class);
        encodePassCode(card);
        Card save = cardRepository.save(card);
        if (save.getId() != null) {
            return new ResponseModel(MessageModel.SUCCESS);
        }
        return new ResponseModel(MessageModel.COULD_NOT_SAVE_RECORD);
    }

    public ResponseModel updateBalance(BalanceDto balanceDto) {
        Optional<Card> byCardNumber = cardRepository.findByCardNumber(balanceDto.getCardNumber());
        Card card = byCardNumber.get();
        card.setBalance(card.getBalance() + balanceDto.getAmount());
        Card save = cardRepository.save(card);
        if (save.getId() != null) {
            return new ResponseModel(MessageModel.SUCCESS);
        }
        return new ResponseModel(MessageModel.COULD_NOT_UPDATE_RECORD);
    }

    private String decrypt(String passCode) {
        String password = "myBankSecretKey";
        String salt = "5c0744940b5c369b";
        TextEncryptor text = Encryptors.text(password, salt);
        return text.decrypt(passCode);
    }

    private void encodePassCode(Card card) {
        card.setPassCode(encoder.encode(card.getPassCode()));
    }

}
