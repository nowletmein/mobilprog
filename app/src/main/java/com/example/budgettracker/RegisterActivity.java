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

                Toast.makeText(this, getString(R.string.fill_all), Toast.LENGTH_SHORT).show();
            } else {
                boolean inserted = myDb.registerUser(user, pass);
                if (inserted) {

                    Toast.makeText(this, getString(R.string.reg_success), Toast.LENGTH_SHORT).show();
                    finish();
                } else {

                    Toast.makeText(this, getString(R.string.reg_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}