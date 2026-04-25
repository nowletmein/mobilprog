package com.example.budgettracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        myDb = new DatabaseHelper(this);

        // UI elemek összekötése
        totalSpentTv = findViewById(R.id.totalSpentTv);
        pieChart = findViewById(R.id.pieChart);
        btnAddExpense = findViewById(R.id.btnAddExpense);

        // Navigáció az új felvétel oldalra
        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, AddExpenseActivity.class);
            startActivity(intent);
        });

        // Első betöltés
        updateStats();
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateStats();
    }

    private void updateStats() {

        double total = myDb.getTotalAmount();
        totalSpentTv.setText("Total: " + total + " Ft");


        List<PieEntry> entries = new ArrayList<>();
        Cursor cursor = myDb.getSummaryByType();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String category = cursor.getString(0);
                float value = cursor.getFloat(1);
                entries.add(new PieEntry(value, category));
            } while (cursor.moveToNext());
            cursor.close();
        }


        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(14f);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setCenterText("Expenses");
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(800);
        pieChart.invalidate();
    }
}