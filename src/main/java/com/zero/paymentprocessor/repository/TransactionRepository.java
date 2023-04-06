/**
 * This interface is used for retrieving, updating, deleting
 * data from transaction table.
 */

package com.zero.paymentprocessor.repository;

import com.zero.paymentprocessor.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
