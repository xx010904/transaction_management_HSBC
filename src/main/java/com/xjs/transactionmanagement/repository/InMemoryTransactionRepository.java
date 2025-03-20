package com.xjs.transactionmanagement.repository;

import com.xjs.transactionmanagement.entity.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryTransactionRepository {

    private final Map<Long, Transaction> transactionMap = new HashMap<>();
    private long currentId = 1;

    // Create
    public Transaction save(Transaction transaction) {
        transaction.setId(currentId++);
        transactionMap.put(transaction.getId(), transaction);
        return transaction;
    }

    // Read
    public List<Transaction> findAll() {
        return new ArrayList<>(transactionMap.values());
    }

    public Optional<Transaction> findById(Long id) {
        return Optional.ofNullable(transactionMap.get(id));
    }

    // Update
    public Transaction update(Long id, Transaction transactionDetails) {
        Transaction transaction = transactionMap.get(id);
        if (transaction != null) {
            transaction.setName(transactionDetails.getName());
            transaction.setAmount(transactionDetails.getAmount());
            return transaction;
        }
        return null;
    }

    // Delete
    public void deleteById(Long id) {
        transactionMap.remove(id);
    }

    public boolean existsById(Long id) {
        return transactionMap.containsKey(id);
    }

    public boolean existsByName(String name) {
        return transactionMap.values().stream()
                .anyMatch(transaction -> transaction.getName().equals(name));
    }

}