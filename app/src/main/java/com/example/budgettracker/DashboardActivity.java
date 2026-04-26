package com.example.budgettracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    TextView totalSpentTv;
    PieChart pieChart;
    Button btnAddExpense;

    // Lista elemek
    RecyclerView recyclerView;
    ExpenseAdapter adapter;
    List<Expense> expenseList;
    String loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        myDb = new DatabaseHelper(this);

        // Lekérjük, ki van belépve
        loggedInUser = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("current_user", "");

        totalSpentTv = findViewById(R.id.totalSpentTv);
        pieChart = findViewById(R.id.pieChart);
        btnAddExpense = findViewById(R.id.btnAddExpense);

        // RecyclerView beállítása
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, AddExpenseActivity.class);
            startActivity(intent);
        });
        ImageButton btnOptions = findViewById(R.id.btnOptions);
        btnOptions.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(DashboardActivity.this, v);
            popup.getMenuInflater().inflate(R.menu.dashboard_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();

                if (id == R.id.menu_logout) {
                    handleLogout();
                    return true;
                } else if (id == R.id.lang_en) {
                    setLocale("en");
                    return true;
                } else if (id == R.id.lang_hu) {
                    setLocale("hu");
                    return true;
                }
                return false;
            });
            popup.show();
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView rv, RecyclerView.ViewHolder vh, RecyclerView.ViewHolder target) {
                return false; // Erre nincs szükségünk (ez az áthelyezés lenne)
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Expense expenseToDelete = adapter.getExpenseAt(position);

                // A megerősítő ablak (Popup)
                new AlertDialog.Builder(DashboardActivity.this)
                        .setTitle("Delete Item")
                        .setMessage("Are you sure you want to delete this?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Törlés az adatbázisból
                            myDb.deleteExpense(expenseToDelete.getId());
                            // Lista frissítése
                            loadExpenses();
                            Toast.makeText(DashboardActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            // Ha "Nem", akkor visszahúzzuk az elemet a helyére
                            adapter.notifyItemChanged(position);
                        })
                        .setCancelable(false) // Ne lehessen mellékattintással bezárni
                        .show();
            }
        }).attachToRecyclerView(recyclerView);

        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI(); // Frissítünk mindent, ha visszajövünk a mentésből
    }

    private void updateUI() {
        updateStats();   // Diagram és összesített egyenleg
        loadExpenses();  // A lista frissítése
    }

    private void updateStats() {
        // 1. Egyenleg megjelenítése (Bevételek - Kiadások)
        double balance = myDb.getBalanceByUser(loggedInUser);
        totalSpentTv.setText(getString(R.string.max_budget, String.valueOf(balance)));

        // 2. Csak a KIADÁSOK (negatív értékek) lekérése a diagramhoz
        List<PieEntry> entries = new ArrayList<>();
        Cursor cursor = myDb.getExpensesOnlySummary(loggedInUser);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String category = cursor.getString(0);
                // Az abszolút értéket vesszük (pl. -200-ból 200), hogy a tortadiagram ki tudja rajzolni
                float value = Math.abs(cursor.getFloat(1));
                entries.add(new PieEntry(value, category));
            } while (cursor.moveToNext());
            cursor.close();
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(14f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setCenterText("Kiadások"); // Csak a költéseket mutatja
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(800);
        pieChart.invalidate();

    }

    private void loadExpenses() {
        expenseList = new ArrayList<>();
        Cursor cursor = myDb.getAllDataByUser(loggedInUser);

        if (cursor != null && cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String amount = cursor.getString(2);
                String date = cursor.getString(3);
                String type = cursor.getString(4);


                expenseList.add(new Expense(id, title, amount, type, date));
            } while (cursor.moveToNext());
            cursor.close();
        }

        adapter = new ExpenseAdapter(expenseList);
        recyclerView.setAdapter(adapter);
    }
    private void handleLogout() {
        // Töröljük az elmentett felhasználót
        getSharedPreferences("UserPrefs", MODE_PRIVATE).edit().clear().apply();

        // Vissza a Login képernyőre
        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setLocale(String langCode) {
        // Ez a kód átállítja az app nyelvét
        java.util.Locale locale = new java.util.Locale(langCode);
        java.util.Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Újraindítjuk az activity-t, hogy látszódjon a változás
        recreate();
    }
}