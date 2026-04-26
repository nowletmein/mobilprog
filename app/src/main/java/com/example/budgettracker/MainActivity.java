package com.example.budgettracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
        ImageButton btnLang = findViewById(R.id.btnLangLogin);
        btnLang.setOnClickListener(v -> showLanguageDialog());

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
            showError(getString(R.string.h_login_error));
            return;
        }

        if (myDb.checkUser(user, pass)) {

            getSharedPreferences("UserPrefs", MODE_PRIVATE)
                    .edit()
                    .putString("current_user", user) // A 'user' változó tartalmazza a beírt nevet
                    .apply(); // Ez véglegesíti a mentést

            // Csak ezután megyünk át a Dashboardra
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
        }else {
            showError(getString(R.string.no_user_found));
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
    private void showLanguageDialog() {
        String[] languages = {"English", "Magyar"};
        new AlertDialog.Builder(this)
                .setTitle("Select Language")
                .setItems(languages, (dialog, which) -> {
                    if (which == 0) setLocale("en");
                    else setLocale("hu");
                }).show();
    }

    private void setLocale(String lang) {
        java.util.Locale locale = new java.util.Locale(lang);
        java.util.Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Újratöltjük az aktivitást, hogy a szövegek frissüljenek
        recreate();
    }
}