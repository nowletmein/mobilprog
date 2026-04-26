package com.example.budgettracker;

public class Expense {
    private int id;
    public String title;
    public String amount;
    public String type;
    public String date;


    public Expense(int id, String title, String amount, String type, String date) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }


    public int getId() {
        return id;
    }
}