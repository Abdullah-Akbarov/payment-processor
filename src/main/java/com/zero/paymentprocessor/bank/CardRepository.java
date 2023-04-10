/**
 * This interface is used for retrieving, updating, deleting
 * data from bank card table.
 */

package com.zero.paymentprocessor.bank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("BankCardRepository")
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByCardNumber(String cardNumber);

    boolean existsByCardNumber(String cardNumber);
}
