/**
 * This interface is used for retrieving, updating, deleting
 * data from transaction table.
 */

package com.zero.paymentprocessor.repository;

import com.zero.paymentprocessor.domain.Transaction;
import com.zero.paymentprocessor.projection.TransactionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<TransactionProjection> findBySenderAndDateTimeBetween(String cardNumber, Timestamp startDate, Timestamp endDate, Pageable pageable);
}
