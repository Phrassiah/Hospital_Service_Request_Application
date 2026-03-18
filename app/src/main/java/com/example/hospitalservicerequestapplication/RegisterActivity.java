package com.example.hospitalservicerequestapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText username, email, password, confirmPass;
    Spinner roleSpinner;
    Button register;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        username = findViewById(R.id.reg_username);
        email = findViewById(R.id.reg_email);
        password = findViewById(R.id.reg_password);
        confirmPass = findViewById(R.id.reg_confirm_password);
        roleSpinner = findViewById(R.id.role_spinner);
        register = findViewById(R.id.btn_register);

        register.setOnClickListener(v -> {
            String user = username.getText().toString();
            String mail = email.getText().toString();
            String pass = password.getText().toString();
            String cPass = confirmPass.getText().toString();
            String role = roleSpinner.getSelectedItem().toString();

            if (user.isEmpty() || mail.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            } else if (!pass.equals(cPass)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                boolean inserted = db.insertUser(user, pass, role);
                if (inserted) {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to Login
                } else {
                    Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}