package com.xjs.transactionmanagement.controller;

import com.xjs.transactionmanagement.entity.Transaction;
import com.xjs.transactionmanagement.entity.TransactionType;
import com.xjs.transactionmanagement.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final int PAGE_SIZE = 10; // Number of records displayed per page

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID must be a positive number.");
        }

        Transaction transaction = transactionService.getTransactionById(id);
        if (transaction == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found.");
        }

        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        if (transaction == null || !isValidTransaction(transaction)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transaction data.");
        }

        Transaction createdTransaction = transactionService.createTransaction(transaction);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(
            @PathVariable Long id,
            @RequestBody Transaction transactionDetails) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID must be a positive number.");
        }

        Transaction updatedTransaction = transactionService.updateTransaction(id, transactionDetails);
        if (updatedTransaction == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found.");
        }

        return ResponseEntity.ok(updatedTransaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID must be a positive number.");
        }

        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    private boolean isValidTransaction(Transaction transaction) {
        // Check that name is not empty
        if (transaction.getName() == null || transaction.getName().trim().isEmpty()) {
            return false; // Name must not be empty
        }
        // Check that amount is non-negative
        if (transaction.getAmount() == null || transaction.getAmount() <= 0) {
            return false; // Amount must not be null and must be greater than 0
        }
        // Check that type is in the enum
        return transaction.getType() != null && isValidType(transaction.getType()); // Type must be a valid enum value
    }

    private boolean isValidType(TransactionType type) {
        for (TransactionType t : TransactionType.values()) {
            if (t == type) {
                return true; // Found a matching type
            }
        }
        return false; // No matching type found
    }

    // Below is for pages
    @GetMapping
    public String listTransactionsPage(@RequestParam(defaultValue = "0") int page, Model model) {
        List<Transaction> pagedTransactions = transactionService.findPaginated(page);
        int totalPages = transactionService.getTotalPages();

        model.addAttribute("transactions", pagedTransactions);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "transaction-list"; // Your HTML template file name
    }

    @GetMapping("/new")
    public String showCreateTransactionFormPage(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "transaction-add"; // Create page
    }

    @PostMapping("/new")
    public String createTransactionPage(@ModelAttribute Transaction transaction) {
        if (transaction == null || !isValidTransaction(transaction)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transaction data.");
        }

        transactionService.createTransaction(transaction);
        return "redirect:/transactions"; // Redirect to transaction list after creation
    }

    @GetMapping("/edit/{id}")
    public String showEditTransactionFormPage(@PathVariable Long id, Model model) {
        Transaction transaction = transactionService.getTransactionById(id);
        model.addAttribute("transaction", transaction);
        return "transaction-edit"; // Return new edit form template
    }

    @PutMapping("/edit/{id}")
    public String updateTransactionPage(
            @PathVariable Long id,
            @ModelAttribute Transaction transaction) {
        if (transaction == null || !isValidTransaction(transaction)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transaction data.");
        }

        Transaction updatedTransaction = transactionService.updateTransaction(id, transaction);
        return "redirect:/transactions"; // Redirect to transaction list after update
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTransactionPage(@PathVariable Long id) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID must be a positive number.");
        }
        transactionService.deleteTransaction(id);
        return "redirect:/transactions"; // Redirect to transaction list after deletion
    }
}