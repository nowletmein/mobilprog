package com.example.budgettracker;

public class Expense {
    private int id;
    public String title;
    public double amount;
    public String type;
    public String date;


    public Expense(int id, String title, double amount, String type, String date) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }


    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }
}