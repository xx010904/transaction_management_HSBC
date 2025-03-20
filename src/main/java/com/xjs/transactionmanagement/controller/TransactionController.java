package com.xjs.transactionmanagement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    @GetMapping("/transactions")
    public String getTransactions() {
        return "List of transactions";
    }
}