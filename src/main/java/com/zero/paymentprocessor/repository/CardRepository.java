/**
 * This interface is used for retrieving, updating, deleting
 * data from card table.
 */

package com.zero.paymentprocessor.repository;

import com.zero.paymentprocessor.domain.Card;
import com.zero.paymentprocessor.domain.User;
import com.zero.paymentprocessor.projection.CardProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByCardNumber(String CardNumber);

    @Query("delete from card c where c.cardNumber = :cardNumber")
    @Modifying
    @Transactional
    Integer deleteCardByCardNumber(@Param(value = "cardNumber") String cardNumber);

    boolean existsByCardNumber(String cardNumber);

    List<CardProjection> findCardByUser(User user);

}
