package com.zero.paymentprocessor.bank;

import com.zero.paymentprocessor.dto.BalanceDto;
import com.zero.paymentprocessor.dto.CardDto;
import com.zero.paymentprocessor.model.MessageModel;
import com.zero.paymentprocessor.model.ResponseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CardService {
    private final PasswordEncoder encoder;
    private final CardRepository cardRepository;
    private final ModelMapper mapper;
    @Value("${secret.key}")
    private String secretKey;

    /**
     * This method retrieves card entity from database.
     *
     * @param cardNumber The card number to find card.
     */
    public ResponseModel getCard(String cardNumber) {
        log.info(">> getCard: cardNumber=" + cardNumber);
        Optional<Card> byCardNumber = cardRepository.findByCardNumber(cardNumber);
        if (byCardNumber.isPresent()) {
            log.info("<< getCard: success");
            return new ResponseModel(MessageModel.SUCCESS);
        }
        log.warn("<< getCard: Not found");
        return new ResponseModel(MessageModel.NOT_FOUND);
    }

    /**
     * This method retrieves card entity from database.
     *
     * @param cardNumber The card number to find card.
     * @return balance of the card;
     */
    public ResponseModel getBalance(String cardNumber) {
        log.info(">> getBalance: cardNumber=" + cardNumber);
        Optional<Card> byCardNumber = cardRepository.findByCardNumber(cardNumber);
        if (byCardNumber.isPresent()) {
            log.info("<< getBalance: Success");
            return new ResponseModel(MessageModel.SUCCESS, byCardNumber.get().getBalance());
        }
        log.warn("<< getBalance: Not found");
        return new ResponseModel(MessageModel.NOT_FOUND);
    }

    /**
     * This method gets card entity and validates.
     *
     * @param cardDto The card data.
     */
    public ResponseModel validateCard(CardDto cardDto) {
        log.info(">> validateCard: " + cardDto);
        Optional<Card> byCardNumber = cardRepository.findByCardNumber(cardDto.getCardNumber());
        if (byCardNumber.isPresent()) {
            String decrypt = decrypt(cardDto.getPassCode());
            if (encoder.matches(decrypt, byCardNumber.get().getPassCode()) && cardDto.getExpireDate().equals(byCardNumber.get().getExpireDate())) {
                log.info("<< validateCard: Success");
                return new ResponseModel(MessageModel.SUCCESS);
            }
            log.warn("<< validateCard: Authentication failed");
            return new ResponseModel(MessageModel.AUTHENTICATION_FAILED);
        }
        log.warn("<< validateCard: Not found");
        return new ResponseModel(MessageModel.NOT_FOUND);
    }

    /**
     * This method saves the provided bank card entity into the database.
     *
     * @param cardSaveDto The card dto to be saved and mapped as entity.
     */
    public ResponseModel saveCard(CardSaveDto cardSaveDto) {
        log.info(">> saveCard: " + cardSaveDto);
        if (cardRepository.existsByCardNumber(cardSaveDto.getCardNumber())) {
            log.warn("<< saveCard: Record already exist");
            return new ResponseModel(MessageModel.RECORD_AlREADY_EXIST);
        }
        Card card = mapper.map(cardSaveDto, Card.class);
        encodePassCode(card);
        Card save = cardRepository.save(card);
        log.info("<< saveCard: Success");
        return new ResponseModel(MessageModel.SUCCESS);
    }

    /**
     * This method gets card entity and updates balance.
     *
     * @param balanceDto The balance details.
     */
    public ResponseModel updateBalance(BalanceDto balanceDto) {
        log.info(">> updateBalance: " + balanceDto);
        Optional<Card> byCardNumber = cardRepository.findByCardNumber(balanceDto.getCardNumber());
        Card card = byCardNumber.get();
        card.setBalance(card.getBalance() + balanceDto.getAmount());
        Card save = cardRepository.save(card);
        log.info("<< updateBalance: Success");
        return new ResponseModel(MessageModel.SUCCESS);
    }

    /**
     * This method is used to decrypt passcode of the card.
     *
     * @param passCode encrypted passcode;
     * @return decrypted passcode.
     */
    private String decrypt(String passCode) {
        String salt = "5c0744940b5c369b";
        TextEncryptor text = Encryptors.text(secretKey, salt);
        return text.decrypt(passCode);
    }

    /**
     * This method is used to encode card passCode to provide more security.
     *
     * @param card card Entity.
     */
    private void encodePassCode(Card card) {
        card.setPassCode(encoder.encode(card.getPassCode()));
    }

}
