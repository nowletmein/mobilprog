package com.example.budgettracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText regUser, regPass;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        myDb = new DatabaseHelper(this);
        regUser = findViewById(R.id.regUsername);
        regPass = findViewById(R.id.regPassword);
        btnRegister = findViewById(R.id.btnRegisterConfirm);

        btnRegister.setOnClickListener(v -> {
            String user = regUser.getText().toString().trim();
            String pass = regPass.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Tölts ki mindent!", Toast.LENGTH_SHORT).show();
            } else {
                boolean inserted = myDb.registerUser(user, pass);
                if (inserted) {
                    Toast.makeText(this, "Sikeres regisztráció!", Toast.LENGTH_SHORT).show();
                    finish(); // Visszazárja ezt az ablakot a Login-hoz
                } else {
                    Toast.makeText(this, "Hiba! Talán már létezik ez a név?", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}