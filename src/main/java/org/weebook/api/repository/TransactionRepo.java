package org.weebook.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.weebook.api.entity.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
}
