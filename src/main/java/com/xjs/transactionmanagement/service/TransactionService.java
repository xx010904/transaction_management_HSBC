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

    private final int PAGE_SIZE = 10; // 每页显示的记录数

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
        // 检查是否已存在相同的交易
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
        // 检查是否存在
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.OK, ErrorCode.TRANSACTION_NOT_FOUND.getMessage()));

        // 检查是否存在同名的交易，但排除当前交易
        if (transactionRepository.existsByName(transactionDetails.getName()) &&
                !existingTransaction.getName().equals(transactionDetails.getName())) {
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.TRANSACTION_ALREADY_EXISTS.getMessage());
        }

        // 更新交易信息
        existingTransaction.setAmount(transactionDetails.getAmount());
        existingTransaction.setName(transactionDetails.getName());
        existingTransaction.setType(transactionDetails.getType());

        return transactionRepository.update(id, existingTransaction);
    }

    // Delete
    public void deleteTransaction(Long id) {
        // 检查是否存在
        if (!transactionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.OK, ErrorCode.TRANSACTION_NOT_FOUND.getMessage());
        }
        transactionRepository.deleteById(id);
    }


}