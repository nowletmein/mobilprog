package com.example.budgettracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    private List<Expense> expenseList;

    public ExpenseAdapter(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.title.setText(expense.title);
        holder.typeDate.setText(expense.type + " | " + expense.date);

        // Színezés az összeg alapján
        double amt = Double.parseDouble(expense.amount);
        holder.amount.setText(expense.amount + " Ft");
        if (amt >= 0) {
            holder.amount.setTextColor(android.graphics.Color.GREEN);
        } else {
            holder.amount.setTextColor(android.graphics.Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }
    public Expense getExpenseAt(int position) {
        return expenseList.get(position);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, typeDate, amount;
        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.itemTitle);
            typeDate = view.findViewById(R.id.itemTypeAndDate);
            amount = view.findViewById(R.id.itemAmount);
        }
    }
}