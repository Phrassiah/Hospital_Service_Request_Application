package com.example.hospitalservicerequestapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PatientRequestActivity extends AppCompatActivity {

    // 1. Declare UI Elements
    EditText etWardBed, etNotes;
    Spinner serviceSpinner;
    Button btnSubmit;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_request); // Ensure this matches your XML filename

        // 2. Initialize Views (Connect to XML IDs)
        etWardBed = findViewById(R.id.et_ward_details); // Based on your red-line screenshot
        etNotes = findViewById(R.id.et_request_notes);
        serviceSpinner = findViewById(R.id.spinner_browse_services);
        btnSubmit = findViewById(R.id.btn_submit_request);
        db = new DatabaseHelper(this);

        // 3. Populate the Spinner with hardcoded data (or from DB)
        setupSpinner();

        // 4. Handle Button Click
        btnSubmit.setOnClickListener(v -> {
            submitRequest();
        });
    }

    private void setupSpinner() {
        String[] services = {"Cleaning", "Equipment assistance", "Linen change", "Porter services"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, services);
        serviceSpinner.setAdapter(adapter);
    }

    private void submitRequest() {
        // GET THE TEXT (The .toString() is what fixes your red errors!)
        String ward = etWardBed.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();
        String selectedService = serviceSpinner.getSelectedItem().toString();

        // VALIDATION
        if (ward.isEmpty()) {
            Toast.makeText(this, "Please enter your Ward and Bed Number", Toast.LENGTH_SHORT).show();
            return;
        }

        // DATABASE INSERT
        boolean isInserted = db.insertRequest(selectedService, ward, notes);

        if (isInserted) {
            Toast.makeText(this, "Application Sent Successfully!", Toast.LENGTH_LONG).show();

            // Clear fields for next use
            etWardBed.setText("");
            etNotes.setText("");

            // Optional: Close screen after success
            // finish();
        } else {
            Toast.makeText(this, "Database Error: Could not save request", Toast.LENGTH_SHORT).show();
        }
    }
}