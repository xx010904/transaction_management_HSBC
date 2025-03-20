package com.xjs.transactionmanagement.controller;

import com.xjs.transactionmanagement.entity.Transaction;
import com.xjs.transactionmanagement.entity.TransactionType;
import com.xjs.transactionmanagement.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private Model model;

    @InjectMocks
    private TransactionController transactionController;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transaction = new Transaction(1L, "Sample Transaction", 100.0, null);
    }

    @Test
    void testGetTransactionById_ShouldReturnTransaction_WhenExists() {
        when(transactionService.getTransactionById(1L)).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.getTransactionById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transaction, response.getBody());
    }

    @Test
    public void testGetTransactionById_ValidId() {
        Long validId = 1L;
        Transaction transaction = new Transaction(validId, "Existing Transaction", 100.0, TransactionType.INCOME);

        when(transactionService.getTransactionById(validId)).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.getTransactionById(validId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transaction, response.getBody());
        verify(transactionService).getTransactionById(validId);
    }

    @Test
    public void testGetTransactionById_NegativeId() {
        Long invalidId = -1L;

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.getTransactionById(invalidId);
        });

        assertEquals("400 BAD_REQUEST \"ID must be a positive number.\"", exception.getMessage());
        verify(transactionService, never()).getTransactionById(anyLong());
    }

    @Test
    public void testGetTransactionById_NotFound() {
        Long validId = 1L;

        when(transactionService.getTransactionById(validId)).thenReturn(null);

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.getTransactionById(validId);
        });

        assertEquals("404 NOT_FOUND \"Transaction not found.\"", exception.getMessage());
        verify(transactionService).getTransactionById(validId);
    }

    @Test
    void testGetTransactionById_ShouldThrowException_WhenNotFound() {
        when(transactionService.getTransactionById(1L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found."));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.getTransactionById(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Transaction not found.", exception.getReason());
    }

    @Test
    public void testCreateTransaction_ValidTransaction() {
        Transaction transaction = new Transaction(1L, "New Transaction", 100.0, TransactionType.EXPENSE);
        when(transactionService.createTransaction(transaction)).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.createTransaction(transaction);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transaction, response.getBody());
        verify(transactionService).createTransaction(transaction);
    }

    @Test
    public void testCreateTransaction_InvalidTransaction() {
        Transaction transaction = null; // 模拟无效交易

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.createTransaction(transaction);
        });

        assertEquals("400 BAD_REQUEST \"Invalid transaction data.\"", exception.getMessage());
        verify(transactionService, never()).createTransaction(any(Transaction.class));
    }

    @Test
    public void testCreateTransaction_InvalidData() {
        Transaction transaction = new Transaction(1L, "", -100.0, null); // 无效数据

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.createTransaction(transaction);
        });

        assertEquals("400 BAD_REQUEST \"Invalid transaction data.\"", exception.getMessage());
        verify(transactionService, never()).createTransaction(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_ShouldThrowException_WhenInvalid() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.createTransaction(null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Invalid transaction data.", exception.getReason());
    }

    @Test
    void testUpdateTransaction_ShouldReturnUpdatedTransaction_WhenExists() {
        when(transactionService.updateTransaction(1L, transaction)).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.updateTransaction(1L, transaction);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transaction, response.getBody());
    }

    @Test
    public void testUpdateTransaction_ValidId() {
        Long validId = 1L;
        Transaction transactionDetails = new Transaction(validId, "Updated Transaction", 150.0, TransactionType.INCOME);
        Transaction updatedTransaction = new Transaction(validId, "Updated Transaction", 150.0, TransactionType.INCOME);

        when(transactionService.updateTransaction(validId, transactionDetails)).thenReturn(updatedTransaction);

        ResponseEntity<Transaction> response = transactionController.updateTransaction(validId, transactionDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTransaction, response.getBody());
        verify(transactionService).updateTransaction(validId, transactionDetails);
    }

    @Test
    public void testUpdateTransaction_NegativeId() {
        Long invalidId = -1L;
        Transaction transactionDetails = new Transaction(invalidId, "Updated Transaction", 150.0, TransactionType.INCOME);

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.updateTransaction(invalidId, transactionDetails);
        });

        assertEquals("400 BAD_REQUEST \"ID must be a positive number.\"", exception.getMessage());
        verify(transactionService, never()).updateTransaction(anyLong(), any(Transaction.class));
    }

    @Test
    public void testUpdateTransaction_NotFound() {
        Long validId = 1L;
        Transaction transactionDetails = new Transaction(validId, "Updated Transaction", 150.0, TransactionType.INCOME);

        when(transactionService.updateTransaction(validId, transactionDetails)).thenReturn(null);

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.updateTransaction(validId, transactionDetails);
        });

        assertEquals("404 NOT_FOUND \"Transaction not found.\"", exception.getMessage());
        verify(transactionService).updateTransaction(validId, transactionDetails);
    }

    @Test
    public void testDeleteTransaction_ValidId() {
        Long validId = 1L;

        ResponseEntity<Void> response = transactionController.deleteTransaction(validId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(transactionService).deleteTransaction(validId);
    }

    @Test
    public void testDeleteTransaction_NegativeId() {
        Long invalidId = -1L;

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.deleteTransaction(invalidId);
        });

        assertEquals("400 BAD_REQUEST \"ID must be a positive number.\"", exception.getMessage());
        verify(transactionService, never()).deleteTransaction(anyLong());
    }

    @Test
    void testUpdateTransaction_ShouldThrowException_WhenNotFound() {
        when(transactionService.updateTransaction(1L, transaction)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found."));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.updateTransaction(1L, transaction);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Transaction not found.", exception.getReason());
    }

    @Test
    void deleteTransaction_ShouldReturnNoContent_WhenDeleted() {
        doNothing().when(transactionService).deleteTransaction(1L);

        ResponseEntity<Void> response = transactionController.deleteTransaction(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteTransaction_ShouldThrowException_WhenNotFound() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found.")).when(transactionService).deleteTransaction(1L);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.deleteTransaction(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Transaction not found.", exception.getReason());
    }

    @Test
    public void testListTransactionsPage() {
        // 准备测试数据
        List<Transaction> pagedTransactions = new ArrayList<>();
        for (long i = 1; i <= 10; i++) {
            pagedTransactions.add(new Transaction(i, "Transaction " + i, i * 10.0, (i % 2 == 0 ? TransactionType.EXPENSE : TransactionType.INCOME)));
        }

        when(transactionService.findPaginated(0)).thenReturn(pagedTransactions);
        when(transactionService.getTotalPages()).thenReturn(3);

        // 调用控制器方法
        String viewName = transactionController.listTransactionsPage(0, model);

        // 验证结果
        assertEquals("transaction-list", viewName);
        verify(model).addAttribute("transactions", pagedTransactions);
        verify(model).addAttribute("currentPage", 0);
        verify(model).addAttribute("totalPages", 3);
    }

    @Test
    void testShowCreateTransactionFormPage_ShouldAddNewTransactionToModel() {
        Model model = mock(Model.class);

        String viewName = transactionController.showCreateTransactionFormPage(model);

        // 使用 any() 来匹配传入的对象
        verify(model, times(1)).addAttribute(eq("transaction"), any(Transaction.class));
        assertEquals("transaction-add", viewName);
    }

    @Test
    public void testCreateTransactionPage_ValidTransaction() {
        Transaction transaction = new Transaction(1L, "New Transaction", 100.0, TransactionType.INCOME);

        String viewName = transactionController.createTransactionPage(transaction);

        assertEquals("redirect:/transactions", viewName);
        verify(transactionService).createTransaction(transaction);
    }

    @Test
    public void testCreateTransactionPage_InvalidTransaction() {
        Transaction transaction = null; // 模拟无效交易

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.createTransactionPage(transaction);
        });

        assertEquals("400 BAD_REQUEST \"Invalid transaction data.\"", exception.getMessage());
        verify(transactionService, never()).createTransaction(any(Transaction.class));
    }

    @Test
    public void testCreateTransactionPage_InvalidData() {
        Transaction transaction = new Transaction(1L, "", -100.0, null); // 无效数据

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.createTransactionPage(transaction);
        });

        assertEquals("400 BAD_REQUEST \"Invalid transaction data.\"", exception.getMessage());
        verify(transactionService, never()).createTransaction(any(Transaction.class));
    }

    @Test
    void testShowEditTransactionFormPage_ShouldAddTransactionToModel() {
        Model model = mock(Model.class);
        when(transactionService.getTransactionById(1L)).thenReturn(transaction);

        String viewName = transactionController.showEditTransactionFormPage(1L, model);

        verify(model, times(1)).addAttribute("transaction", transaction);
        assertEquals("transaction-edit", viewName);
    }

    @Test
    public void testUpdateTransactionPage_ValidTransaction() {
        Long transactionId = 1L;
        Transaction transaction = new Transaction(transactionId, "Updated Transaction", 100.0, TransactionType.EXPENSE);

        when(transactionService.updateTransaction(transactionId, transaction)).thenReturn(transaction);

        String viewName = transactionController.updateTransactionPage(transactionId, transaction);

        assertEquals("redirect:/transactions", viewName);
        verify(transactionService).updateTransaction(transactionId, transaction);
    }

    @Test
    public void testUpdateTransactionPage_InvalidTransaction() {
        Long transactionId = 1L;
        Transaction transaction = null; // 模拟无效交易

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.updateTransactionPage(transactionId, transaction);
        });

        assertEquals("400 BAD_REQUEST \"Invalid transaction data.\"", exception.getMessage());
        verify(transactionService, never()).updateTransaction(anyLong(), any(Transaction.class));
    }

    @Test
    public void testUpdateTransactionPage_InvalidData() {
        Long transactionId = 1L;
        Transaction transaction = new Transaction(transactionId, "", -100.0, null); // 无效数据

        when(transactionService.updateTransaction(transactionId, transaction)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            transactionController.updateTransactionPage(transactionId, transaction);
        });

        assertEquals("400 BAD_REQUEST \"Invalid transaction data.\"", exception.getMessage());
        verify(transactionService, never()).updateTransaction(anyLong(), any(Transaction.class));
    }

    @Test
    void testDeleteTransactionPage_ShouldRedirect_WhenDeleted() {
        String viewName = transactionController.deleteTransactionPage(1L);

        assertEquals("redirect:/transactions", viewName);
    }
}