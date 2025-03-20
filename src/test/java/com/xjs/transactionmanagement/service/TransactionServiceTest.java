package com.xjs.transactionmanagement.service;

import com.xjs.transactionmanagement.entity.Transaction;
import com.xjs.transactionmanagement.entity.TransactionType;
import com.xjs.transactionmanagement.exception.ErrorCode;
import com.xjs.transactionmanagement.repository.InMemoryTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private InMemoryTransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transaction = new Transaction(1L, "Sample Transaction", 100.0, null);
    }

    @Test
    public void testFindPaginated() {
        // 准备测试数据
        List<Transaction> allTransactions = new ArrayList<>();
        for (long i = 1; i <= 25; i++) {
            allTransactions.add(new Transaction(i, "Transaction " + i, i * 10.0, (i % 2 == 0 ? TransactionType.EXPENSE : TransactionType.INCOME)));
        }

        when(transactionRepository.findAll()).thenReturn(allTransactions);

        // 测试第一页
        List<Transaction> firstPage = transactionService.findPaginated(0);
        assertEquals(10, firstPage.size()); // 应该返回10条记录

        // 测试第二页
        List<Transaction> secondPage = transactionService.findPaginated(1);
        assertEquals(10, secondPage.size()); // 应该返回10条记录

        // 测试第三页
        List<Transaction> thirdPage = transactionService.findPaginated(2);
        assertEquals(5, thirdPage.size()); // 应该返回5条记录
    }

    @Test
    public void testGetTotalPages() {
        // 准备测试数据
        List<Transaction> allTransactions = new ArrayList<>();
        for (long i = 1; i <= 25; i++) {
            allTransactions.add(new Transaction(i, "Transaction " + i, i * 10.0, (i % 2 == 0 ? TransactionType.EXPENSE : TransactionType.INCOME)));
        }

        when(transactionRepository.findAll()).thenReturn(allTransactions);

        int totalPages = transactionService.getTotalPages();
        assertEquals(3, totalPages); // 25条记录，PAGE_SIZE为10，总页数应为3
    }

    @Test
    void testCreateTransaction_ShouldThrowException_WhenTransactionAlreadyExists() {
        when(transactionRepository.existsById(transaction.getId())).thenReturn(true);
        when(transactionRepository.existsByName(transaction.getName())).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionService.createTransaction(transaction);
        });

        assertEquals(HttpStatus.OK, exception.getStatusCode());
        assertEquals(ErrorCode.TRANSACTION_ALREADY_EXISTS.getMessage(), exception.getReason());
    }

    @Test
    void testCreateTransaction_ShouldThrowException_WhenTransactionNameAlreadyExists() {
        when(transactionRepository.existsById(transaction.getId())).thenReturn(false);
        when(transactionRepository.existsByName(transaction.getName())).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionService.createTransaction(transaction);
        });

        assertEquals(HttpStatus.OK, exception.getStatusCode());
        assertEquals(ErrorCode.TRANSACTION_ALREADY_EXISTS.getMessage(), exception.getReason());
    }

    @Test
    public void testGetAllTransactions() {
        // 准备测试数据
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(1L, "Transaction 1", 100.0, TransactionType.INCOME));
        transactions.add(new Transaction(2L, "Transaction 2", 200.0, TransactionType.EXPENSE));

        // 模拟 repository 的行为
        when(transactionRepository.findAll()).thenReturn(transactions);

        // 调用服务方法
        List<Transaction> result = transactionService.getAllTransactions();

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(transactions, result);
        verify(transactionRepository).findAll(); // 验证 repository 的调用
    }

    @Test
    void testGetTransactionById_ShouldThrowException_WhenNotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionService.getTransactionById(1L);
        });

        assertEquals(HttpStatus.OK, exception.getStatusCode());
        assertEquals(ErrorCode.TRANSACTION_NOT_FOUND.getMessage(), exception.getReason());
    }

    @Test
    void testUpdateTransaction_ShouldThrowException_WhenNotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionService.updateTransaction(1L, transaction);
        });

        assertEquals(HttpStatus.OK, exception.getStatusCode());
        assertEquals(ErrorCode.TRANSACTION_NOT_FOUND.getMessage(), exception.getReason());
    }

    @Test
    void testUpdateTransaction_ShouldThrowException_WhenNameAlreadyExists() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.existsByName(any())).thenReturn(true);
        Transaction newTransaction = new Transaction(1L, "Existing Transaction", 200.0, null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionService.updateTransaction(1L, newTransaction);
        });

        assertEquals(HttpStatus.OK, exception.getStatusCode());
        assertEquals(ErrorCode.TRANSACTION_ALREADY_EXISTS.getMessage(), exception.getReason());
    }

    @Test
    void testDeleteTransaction_ShouldThrowException_WhenNotFound() {
        when(transactionRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionService.deleteTransaction(1L);
        });

        assertEquals(HttpStatus.OK, exception.getStatusCode());
        assertEquals(ErrorCode.TRANSACTION_NOT_FOUND.getMessage(), exception.getReason());
    }

    @Test
    public void testUpdateTransaction_Valid() {
        Long validId = 1L;
        Transaction existingTransaction = new Transaction(validId, "Existing Transaction", 100.0, TransactionType.INCOME);
        Transaction transactionDetails = new Transaction(validId, "Updated Transaction", 150.0, TransactionType.EXPENSE);

        when(transactionRepository.findById(validId)).thenReturn(java.util.Optional.of(existingTransaction));
        when(transactionRepository.existsByName(transactionDetails.getName())).thenReturn(false);
        when(transactionRepository.update(validId, existingTransaction)).thenReturn(existingTransaction);

        Transaction updatedTransaction = transactionService.updateTransaction(validId, transactionDetails);

        assertEquals("Updated Transaction", updatedTransaction.getName());
        assertEquals(150.0, updatedTransaction.getAmount());
        assertEquals(TransactionType.EXPENSE, updatedTransaction.getType());
        verify(transactionRepository).update(validId, existingTransaction);
    }

    @Test
    public void testUpdateTransaction_NotFound() {
        Long invalidId = 2L;
        Transaction transactionDetails = new Transaction(invalidId, "Updated Transaction", 150.0, TransactionType.EXPENSE);

        when(transactionRepository.findById(invalidId)).thenReturn(java.util.Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionService.updateTransaction(invalidId, transactionDetails);
        });

        verify(transactionRepository, never()).update(anyLong(), any(Transaction.class));
    }

    @Test
    public void testUpdateTransaction_AlreadyExists() {
        Long validId = 1L;
        Transaction existingTransaction = new Transaction(validId, "Existing Transaction", 100.0, TransactionType.INCOME);
        Transaction transactionDetails = new Transaction(validId, "Existing Transaction", 150.0, TransactionType.EXPENSE);

        when(transactionRepository.findById(validId)).thenReturn(java.util.Optional.of(existingTransaction));
        when(transactionRepository.existsByName(transactionDetails.getName())).thenReturn(true);

        verify(transactionRepository, never()).update(anyLong(), any(Transaction.class));
    }

    @Test
    public void testDeleteTransaction_ValidId() {
        Long validId = 1L;

        when(transactionRepository.existsById(validId)).thenReturn(true);

        transactionService.deleteTransaction(validId);
        verify(transactionRepository).deleteById(validId);
    }

    @Test
    public void testDeleteTransaction_NotFound() {
        Long invalidId = 2L;

        when(transactionRepository.existsById(invalidId)).thenReturn(false);

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionService.deleteTransaction(invalidId);
        });

        verify(transactionRepository, never()).deleteById(anyLong());
    }
}