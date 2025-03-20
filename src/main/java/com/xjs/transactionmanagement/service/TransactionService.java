package com.xjs.transactionmanagement.service;

import com.xjs.transactionmanagement.entity.Transaction;
import com.xjs.transactionmanagement.exception.ErrorCode;
import com.xjs.transactionmanagement.repository.InMemoryTransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TransactionService {

    private final InMemoryTransactionRepository transactionRepository;

    private final int PAGE_SIZE = 10; // Number of records displayed per page

    public TransactionService(InMemoryTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> findPaginated(int page) {
        List<Transaction> transactions = transactionRepository.findAll();
        int start = page * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, transactions.size());
        return transactions.subList(start, end);
    }

    public int getTotalPages() {
        int totalTransactions = transactionRepository.findAll().size();
        return (int) Math.ceil((double) totalTransactions / PAGE_SIZE);
    }

    // Create
    public Transaction createTransaction(Transaction transaction) {
        // Check if a transaction with the same ID or name already exists
        if (transactionRepository.existsById(transaction.getId())
                || transactionRepository.existsByName(transaction.getName())) {
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.TRANSACTION_ALREADY_EXISTS.getMessage());
        }
        return transactionRepository.save(transaction);
    }

    // Read
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ErrorCode.TRANSACTION_NOT_FOUND.getMessage()));
    }

    // Update
    public Transaction updateTransaction(Long id, Transaction transactionDetails) {
        // Check if the transaction exists
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ErrorCode.TRANSACTION_NOT_FOUND.getMessage()));

        // Check if a transaction with the same name exists, excluding the current transaction
        if (transactionRepository.existsByName(transactionDetails.getName()) &&
                !existingTransaction.getName().equals(transactionDetails.getName())) {
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.TRANSACTION_ALREADY_EXISTS.getMessage());
        }

        // Update transaction information
        existingTransaction.setAmount(transactionDetails.getAmount());
        existingTransaction.setName(transactionDetails.getName());
        existingTransaction.setType(transactionDetails.getType());

        return transactionRepository.update(id, existingTransaction);
    }

    // Delete
    public void deleteTransaction(Long id) {
        // Check if the transaction exists
        if (!transactionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.TRANSACTION_NOT_FOUND.getMessage());
        }
        transactionRepository.deleteById(id);
    }
}