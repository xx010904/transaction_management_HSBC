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

    private final int PAGE_SIZE = 10; // 每页显示的记录数

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
        // 检查 name 是否非空
        if (transaction.getName() == null || transaction.getName().trim().isEmpty()) {
            return false; // name 不能为空
        }
        // 检查 amount 是否非负
        if (transaction.getAmount() == null || transaction.getAmount() <= 0) {
            return false; // amount 不能为空且必须大于 0
        }
        // 检查 type 是否在枚举里面
        return transaction.getType() != null && isValidType(transaction.getType()); // type 必须是有效的枚举值
    }

    private boolean isValidType(TransactionType type) {
        for (TransactionType t : TransactionType.values()) {
            if (t == type) {
                return true; // 找到匹配的类型
            }
        }
        return false; // 没有匹配的类型
    }


    // below is for pages
//    @GetMapping
//    public String getAllTransactionsPage(Model model) {
//        List<Transaction> transactions = transactionService.getAllTransactions();
//        model.addAttribute("transactions", transactions);
//        return "transaction-list"; // 返回交易列表模板
//    }

    @GetMapping
    public String listTransactionsPage(@RequestParam(defaultValue = "0") int page, Model model) {
        List<Transaction> pagedTransactions = transactionService.findPaginated(page);
        int totalPages = transactionService.getTotalPages();

        model.addAttribute("transactions", pagedTransactions);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "transaction-list"; // 你的 HTML 模板文件名
    }

    @GetMapping("/new")
    public String showCreateTransactionFormPage(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "transaction-add"; // 创建页面
    }

    @PostMapping("/new")
    public String createTransactionPage(@ModelAttribute Transaction transaction) {
        if (transaction == null || !isValidTransaction(transaction)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transaction data.");
        }

        transactionService.createTransaction(transaction);
        return "redirect:/transactions"; // 创建后重定向到交易列表
    }

    @GetMapping("/edit/{id}")
    public String showEditTransactionFormPage(@PathVariable Long id, Model model) {
        Transaction transaction = transactionService.getTransactionById(id);
        model.addAttribute("transaction", transaction);
        return "transaction-edit"; // 返回新的编辑表单模板
    }

    @PutMapping("/edit/{id}")
    public String updateTransactionPage(
            @PathVariable Long id,
            @ModelAttribute Transaction transaction) {
        if (transaction == null || !isValidTransaction(transaction)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transaction data.");
        }

        Transaction updatedTransaction = transactionService.updateTransaction(id, transaction);
        return "redirect:/transactions"; // 创建后重定向到交易列表
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTransactionPage(@PathVariable Long id) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID must be a positive number.");
        }
        transactionService.deleteTransaction(id);
        return "redirect:/transactions"; // 创建后重定向到交易列表
    }
}