package com.example.hospitalservicerequestapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    EditText serviceInput;
    Button addBtn, removeServiceBtn, viewRequestsBtn, viewUsersBtn;
    ListView listView;
    DatabaseHelper db;

    ArrayList<String> servicesList;
    ArrayList<String> usersList;
    ArrayList<String> requestsList;

    String mode = "services";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        serviceInput = findViewById(R.id.serviceInput);
        addBtn = findViewById(R.id.addBtn);
        removeServiceBtn = findViewById(R.id.removeServiceBtn);
        viewRequestsBtn = findViewById(R.id.viewRequestsBtn);
        viewUsersBtn = findViewById(R.id.viewUsersBtn);
        listView = findViewById(R.id.listView);

        db = new DatabaseHelper(this);

        loadServices();

        addBtn.setOnClickListener(v -> {
            String serviceName = serviceInput.getText().toString().trim();
            if (serviceName.isEmpty()) {
                Toast.makeText(this, "Enter service name first", Toast.LENGTH_SHORT).show();
            } else {
                // Ensure addHospitalService is defined in DatabaseHelper
                boolean success = db.addHospitalService(serviceName, "Standard Service");
                if (success) {
                    Toast.makeText(this, "Service Added!", Toast.LENGTH_SHORT).show();
                    serviceInput.setText("");
                    loadServices();
                }
            }
        });

        removeServiceBtn.setOnClickListener(v -> {
            loadServices();
            Toast.makeText(this, "Long press a service to remove it", Toast.LENGTH_SHORT).show();
        });

        viewRequestsBtn.setOnClickListener(v -> {
            loadRequests();
            Toast.makeText(this, "Viewing Patient Applications", Toast.LENGTH_SHORT).show();
        });

        viewUsersBtn.setOnClickListener(v -> {
            loadUsers();
            Toast.makeText(this, "Long press a user to delete them", Toast.LENGTH_SHORT).show();
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            // Check for placeholder text before deleting
            String selectedItem = (String) parent.getItemAtPosition(position);
            if (selectedItem.contains("No data") || selectedItem.contains("No patient")) return true;

            if (mode.equals("services")) {
                String name = servicesList.get(position);
                db.getWritableDatabase().execSQL("DELETE FROM services WHERE s_name=?", new String[]{name});
                Toast.makeText(this, "Service Deleted", Toast.LENGTH_SHORT).show();
                loadServices();
            }
            else if (mode.equals("users")) {
                String username = usersList.get(position);
                db.getWritableDatabase().execSQL("DELETE FROM users WHERE username=?", new String[]{username});
                Toast.makeText(this, "User Deleted", Toast.LENGTH_SHORT).show();
                loadUsers();
            }
            else if (mode.equals("requests")) {
                deleteRequest(position);
                Toast.makeText(this, "Application Resolved", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    void loadServices() {
        mode = "services";
        servicesList = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery("SELECT * FROM services", null);
        int nameIndex = c.getColumnIndex("s_name"); // Fetch index by name for safety

        while (c.moveToNext()) {
            servicesList.add(c.getString(nameIndex != -1 ? nameIndex : 1));
        }
        c.close();
        updateUI(servicesList);
    }

    void loadUsers() {
        mode = "users";
        usersList = new ArrayList<>();
        Cursor c = db.getReadableDatabase().rawQuery("SELECT * FROM users", null);
        int userIndex = c.getColumnIndex("username");

        while (c.moveToNext()) {
            usersList.add(c.getString(userIndex != -1 ? userIndex : 1));
        }
        c.close();
        updateUI(usersList);
    }

    void loadRequests() {
        mode = "requests";
        requestsList = new ArrayList<>();
        Cursor c = db.getAllRequests(); // Uses the method in DatabaseHelper

        if (c != null && c.moveToFirst()) {
            // SAFE LOOKUP: Finding columns by name prevents "No Data" errors
            int sCol = c.getColumnIndex("service");
            int wCol = c.getColumnIndex("ward");
            int nCol = c.getColumnIndex("notes");

            do {
                String s = (sCol != -1) ? c.getString(sCol) : "N/A";
                String w = (wCol != -1) ? c.getString(wCol) : "N/A";
                String n = (nCol != -1) ? c.getString(nCol) : "N/A";
                requestsList.add("Service: " + s + "\nWard: " + w + "\nNotes: " + n);
            } while (c.moveToNext());
            c.close();
        }
        updateUI(requestsList);
    }

    void updateUI(ArrayList<String> list) {
        ArrayList<String> displayList = new ArrayList<>(list);
        if (displayList.isEmpty()) {
            displayList.add("No data available in this section.");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, displayList);
        listView.setAdapter(adapter);
    }

    void deleteRequest(int position) {
        // Use ID for precise deletion
        Cursor c = db.getReadableDatabase().rawQuery("SELECT id FROM requests", null);
        if (c.moveToPosition(position)) {
            int id = c.getInt(0);
            db.getWritableDatabase().execSQL("DELETE FROM requests WHERE id=?", new Object[]{id});
        }
        c.close();
        loadRequests();
    }
}