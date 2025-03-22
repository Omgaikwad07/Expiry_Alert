package com.example.expiry_alert;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ManualEntryActivity extends AppCompatActivity {

    private EditText productName, productExpiry;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

        productName = findViewById(R.id.product_name);
        productExpiry = findViewById(R.id.product_expiry);
        saveButton = findViewById(R.id.save_product);

        saveButton.setOnClickListener(v -> {
            String name = productName.getText().toString();
            String expiry = productExpiry.getText().toString();

            if (name.isEmpty() || expiry.isEmpty()) {
                Toast.makeText(ManualEntryActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ManualEntryActivity.this, "Product Saved!", Toast.LENGTH_SHORT).show();
                finish();  // Close activity after saving
            }
        });
    }
}
