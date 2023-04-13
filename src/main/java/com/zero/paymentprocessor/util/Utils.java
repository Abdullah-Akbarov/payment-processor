package com.zero.paymentprocessor.util;

import com.zero.paymentprocessor.domain.Card;
import com.zero.paymentprocessor.domain.User;
import com.zero.paymentprocessor.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Utils {
    private final CardRepository cardRepository;

    /**
     * This method is for authorizing user for some action.
     *
     * @param cardNumber
     * @return User entity.
     */
    public User isAuthorized(String cardNumber) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Optional<Card> byCardNumber = cardRepository.findByCardNumber(cardNumber);
        if (!byCardNumber.isPresent()) {
            return null;
        }
        return byCardNumber.get().getUser().getId() == user.getId() ? user : null;
    }
}
