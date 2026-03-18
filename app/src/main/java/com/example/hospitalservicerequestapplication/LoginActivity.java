package com.example.hospitalservicerequestapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // 1. Declare variables
    EditText etUser, etPass;
    Button btnLogin;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 2. Initialize (Check your XML IDs match these)
        etUser = findViewById(R.id.login_username);
        etPass = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.btn_login_submit);
        db = new DatabaseHelper(this);

        // 3. Login Logic
        btnLogin.setOnClickListener(v -> {
            String user = etUser.getText().toString().trim();
            String pass = etPass.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else {
                // This calls the method that returns "Admin" or "Patient"
                String role = db.getUserRole(user, pass);

                if (role != null) {
                    Toast.makeText(this, "Login Successful as " + role, Toast.LENGTH_SHORT).show();

                    // Redirect based on role
                    if (role.equalsIgnoreCase("Admin")) {
                        startActivity(new Intent(this, AdminActivity.class));
                    } else {
                        startActivity(new Intent(this, PatientRequestActivity.class));
                    }
                    finish();
                } else {
                    // This triggers if username/password is wrong
                    Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}