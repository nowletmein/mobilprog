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
        String loggedInUser = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("current_user", "");
        myDb = new DatabaseHelper(this);
        editTitle = findViewById(R.id.editTitle);
        editAmount = findViewById(R.id.editAmount);
        typeSpinner = findViewById(R.id.typeSpinner);
        saveBtn = findViewById(R.id.saveBtn);
        Button cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(v -> finish());
        saveBtn.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String amountStr = editAmount.getText().toString().trim();
            String type = typeSpinner.getSelectedItem().toString();

            if (!title.isEmpty() && !amountStr.isEmpty()) {
                try {
                    double amount = Double.parseDouble(amountStr);
                    String date = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(new java.util.Date());

                        // ITT A VÁLTOZÁS: Átadjuk a loggedInUser-t is az 5. paraméternek!
                    if(myDb.insertData(title, amount, date, type, loggedInUser)) {
                        Toast.makeText(this, "Mentve!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Hiba a mentés során!", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Érvénytelen összeg!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Tölts ki mindent!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}