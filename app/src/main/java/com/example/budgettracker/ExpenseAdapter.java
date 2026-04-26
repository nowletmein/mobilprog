package com.example.budgettracker;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    private List<Expense> expenseList;
    private Context context; // Ez kell a fordításhoz (getString)

    public ExpenseAdapter(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext(); // Itt mentjük el a context-et
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense currentExpense = expenseList.get(position);

        holder.title.setText(currentExpense.getTitle());

        // Összeg beállítása
        double amountValue = currentExpense.getAmount();
        holder.amount.setText(amountValue + " Ft");
        holder.amount.setTextColor(amountValue >= 0 ? Color.GREEN : Color.RED);

        // --- FORDÍTÁS ---
        String dbKey = currentExpense.getType();
        String localizedType;

        if ("FOOD".equals(dbKey)) {
            localizedType = context.getString(R.string.food);
        } else if ("ESSENTIAL".equals(dbKey)) {
            localizedType = context.getString(R.string.essential);
        } else {
            localizedType = context.getString(R.string.other);
        }

        // --- ITT VOLT A HIBA: A dátumot a getDate()-ből kérjük le! ---
        holder.typeDate.setText(localizedType + " | " + currentExpense.getDate());
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
            // Ezeknek egyezniük kell az item_expense.xml ID-jaival!
            title = view.findViewById(R.id.itemTitle);
            typeDate = view.findViewById(R.id.itemTypeAndDate);
            amount = view.findViewById(R.id.itemAmount);
        }
    }
}