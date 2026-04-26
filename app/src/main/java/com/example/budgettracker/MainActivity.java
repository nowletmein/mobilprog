package com.example.budgettracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText username, password;
    Button loginBtn, goToRegisterBtn; // Új gomb a regisztrációhoz
    DatabaseHelper myDb; // Ez hiányzott!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this); // Inicializálás

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        goToRegisterBtn = findViewById(R.id.goToRegisterBtn); // Add hozzá az XML-hez is!

        loginBtn.setOnClickListener(v -> handleLogin());


        // Átlépés a regisztrációs oldalra
        goToRegisterBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    public void handleLogin() {
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            showError("Töltsd ki a mezőket!");
            return;
        }

        if (myDb.checkUser(user, pass)) {
            // Itt mentjük el a "jegyzetbe" a felhasználónevet
            getSharedPreferences("UserPrefs", MODE_PRIVATE)
                    .edit()
                    .putString("current_user", user) // A 'user' változó tartalmazza a beírt nevet
                    .apply(); // Ez véglegesíti a mentést

            // Csak ezután megyünk át a Dashboardra
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
        }else {
            showError("Hibás adatok vagy nem létező felhasználó!");
        }
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}