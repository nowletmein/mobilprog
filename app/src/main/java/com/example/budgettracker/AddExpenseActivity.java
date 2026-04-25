package com.example.budgettracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.budgettracker.DatabaseHelper;

public class AddExpenseActivity extends AppCompatActivity {
    EditText editTitle, editAmount;
    Spinner typeSpinner;
    Button saveBtn;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        myDb = new DatabaseHelper(this);
        editTitle = findViewById(R.id.editTitle);
        editAmount = findViewById(R.id.editAmount);
        typeSpinner = findViewById(R.id.typeSpinner);
        saveBtn = findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String amountStr = editAmount.getText().toString().trim();
            String type = typeSpinner.getSelectedItem().toString();

            if (!title.isEmpty() && !amountStr.isEmpty()) {
                double amount = Double.parseDouble(amountStr);
                String date = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(new java.util.Date());

                if(myDb.insertData(title, amount, date, type)) {
                    Toast.makeText(this, "Mentve!", Toast.LENGTH_SHORT).show();
                    finish(); // Bezárja ezt az ablakot és visszavisz a Dashboardra
                }
            } else {
                Toast.makeText(this, "Tölts ki mindent!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}