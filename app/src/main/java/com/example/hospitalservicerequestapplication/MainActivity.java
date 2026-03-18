package com.example.hospitalservicerequestapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnLogin, btnRegister, btnAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Buttons
        btnLogin = findViewById(R.id.btn_main_login);
        btnRegister = findViewById(R.id.btn_main_register);
        btnAdmin = findViewById(R.id.btn_main_admin);

        // Navigate to Login
        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

        // Navigate to Register
        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });

        // Navigate to Admin Panel
        // Inside onCreate in MainActivity.java
        btnAdmin = findViewById(R.id.btn_main_admin);

        btnAdmin.setOnClickListener(v -> {
            // Create an input field for the popup
            final EditText passwordInput = new EditText(this);
            passwordInput.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordInput.setHint("Enter Admin Password");

            // Create the Popup (AlertDialog)
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Admin Authentication")
                    .setMessage("Please enter the secret key to access Admin Panel:")
                    .setView(passwordInput)
                    .setPositiveButton("Login", (dialog, which) -> {
                        String enteredPass = passwordInput.getText().toString();

                        // Set your secret password here
                        if (enteredPass.equals("admin254")) {
                            Toast.makeText(this, "Access Granted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, AdminActivity.class));
                        } else {
                            Toast.makeText(this, "Incorrect Password! Access Denied.", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }
}