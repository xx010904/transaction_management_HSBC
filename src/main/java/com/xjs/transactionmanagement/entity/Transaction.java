package com.xjs.transactionmanagement.entity;


public class Transaction {

    private Long id;
    private String name;
    private Double amount;
    private TransactionType type;

    public Transaction() {}

    public Transaction(Long id, String name, Double amount, TransactionType type) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public TransactionType getType() { // 更新 Getter
        return type;
    }

    public void setType(TransactionType type) { // 更新 Setter
        this.type = type;
    }
}