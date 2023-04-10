/**
 * This interface is used for retrieving, updating, deleting
 * data from user table.
 */


package com.zero.paymentprocessor.repository;

import com.zero.paymentprocessor.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
