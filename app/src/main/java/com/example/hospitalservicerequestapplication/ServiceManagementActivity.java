package com.example.hospitalservicerequestapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ServiceManagementActivity extends AppCompatActivity {
    EditText etName, etDesc;
    Button btnAdd;
    Button btnDelete;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_management);

        // Inside onCreate of ServiceManagementActivity
        EditText etName = findViewById(R.id.et_new_service_name); // Check your XML IDs!
        EditText etDesc = findViewById(R.id.et_new_service_desc);
        Button btnAdd = findViewById(R.id.btn_save_service_now); // The green button
        Button btnDelete = findViewById(R.id.btn_delete_services); // The green button


        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String desc = etDesc.getText().toString().trim();

            if (name.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                // CALL THE DATABASE
                boolean success = db.addHospitalService(name, desc);

                if (success) {
                    Toast.makeText(this, "Service Added Successfully!", Toast.LENGTH_SHORT).show();
                    etName.setText(""); // Clear the boxes
                    etDesc.setText("");
                    finish(); // Goes back to the Admin Panel automatically
                } else {
                    Toast.makeText(this, "Database Error", Toast.LENGTH_SHORT).show();
                    // INTENT: Go back to AdminActivity to see the updated state
                    Intent intent = new Intent(ServiceManagementActivity.this, AdminActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clears the activity stack
                    startActivity(intent);
                    finish();

                }
            }
        });


        btnDelete.setOnClickListener(v -> {
            String nameToDelete = btnDelete.getText().toString().trim();

            if (nameToDelete.isEmpty()) {
                Toast.makeText(this, "Enter a service name to delete", Toast.LENGTH_SHORT).show();
            } else {
                boolean isDeleted = db.deleteService(nameToDelete);
                if (isDeleted) {
                    Toast.makeText(this, "Service Deleted!", Toast.LENGTH_SHORT).show();
                   btnDelete.setText(""); // Clear input
                    // refreshYourList(); // Call your method to update the UI list
                } else {
                    Toast.makeText(this, "Service not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}