////package com.example.expiry_alert;
////
////import android.os.Bundle;
////import android.widget.Button;
////import android.widget.EditText;
////import android.widget.Toast;
////
////import androidx.appcompat.app.AppCompatActivity;
////
////public class ManualEntryActivity extends AppCompatActivity {
////
////    EditText editName, editBrand, editExpiry;
////    Button btnSave;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_manual_entry);
////
////        editName = findViewById(R.id.editProductName);
////        editBrand = findViewById(R.id.editBrandName);
////        editExpiry = findViewById(R.id.editExpiryDate);
////        btnSave = findViewById(R.id.btnSave);
////
////        // Get data from intent
////        String name = getIntent().getStringExtra("name");
////        String brand = getIntent().getStringExtra("brand");
////        String expiryDate = getIntent().getStringExtra("expiryDate");
////
////        // Set prefilled values
////        editName.setText(name);
////        editBrand.setText(brand);
////        editExpiry.setText(expiryDate);
////
////        btnSave.setOnClickListener(v -> {
////            // You can later add Firebase saving logic here
////            String productName = editName.getText().toString().trim();
////            String productBrand = editBrand.getText().toString().trim();
////            String productExpiry = editExpiry.getText().toString().trim();
////
////            if (!productName.isEmpty() && !productExpiry.isEmpty()) {
////                Toast.makeText(this, "Product Saved Locally (Firebase code pending)", Toast.LENGTH_SHORT).show();
////                finish();
////            } else {
////                Toast.makeText(this, "Name and Expiry required!", Toast.LENGTH_SHORT).show();
////            }
////        });
////    }
////}
//package com.example.expiry_alert;
//
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class ManualEntryActivity extends AppCompatActivity {
//
//    EditText etName, etBrand, etExpiryDate;
//    Button btnSave;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_manual_entry);
//
//        etName = findViewById(R.id.editProductName);
//        etBrand = findViewById(R.id.editBrandName);
//        etExpiryDate = findViewById(R.id.editExpiryDate);
//        btnSave = findViewById(R.id.btnSave);
//
//        // Get prefilled data from intent (if coming from scanner)
//        if (getIntent() != null) {
//            etName.setText(getIntent().getStringExtra("name"));
//            etBrand.setText(getIntent().getStringExtra("brand"));
//            etExpiryDate.setText(getIntent().getStringExtra("expiryDate"));
//        }
//
//        btnSave.setOnClickListener(v -> saveProductToFirebase());
//    }
//
//    private void saveProductToFirebase() {
//        String name = etName.getText().toString().trim();
//        String brand = etBrand.getText().toString().trim();
//        String expiryDate = etExpiryDate.getText().toString().trim();
//
//        // ✅ Get logged-in username from SharedPreferences
//        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
//        String username = sharedPreferences.getString("loggedInUsername", null);
//
//        if (username == null) {
//            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // ✅ Validate input
//        if (name.isEmpty() || brand.isEmpty() || expiryDate.isEmpty()) {
//            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // ✅ Save product under user in Firebase
//        DatabaseReference productRef = FirebaseDatabase.getInstance()
//                .getReference("users")
//                .child(username)
//                .child("products");
//
//        String productId = productRef.push().getKey();
//
//        if (productId != null) {
//            ProductModel product = new ProductModel(name, brand, expiryDate);
//            productRef.child(productId).setValue(product)
//                    .addOnSuccessListener(aVoid -> {
//                        Toast.makeText(this, "Product saved successfully!", Toast.LENGTH_SHORT).show();
//                        finish(); // Close activity
//                    })
//                    .addOnFailureListener(e -> {
//                        Toast.makeText(this, "Failed to save product!", Toast.LENGTH_SHORT).show();
//                    });
//        }
//    }
//}
package com.example.expiry_alert;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ManualEntryActivity extends AppCompatActivity {

    EditText etName, etBrand, etExpiryDate;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

        etName = findViewById(R.id.editProductName);
        etBrand = findViewById(R.id.editBrandName);
        etExpiryDate = findViewById(R.id.editExpiryDate);
        btnSave = findViewById(R.id.btnSave);

        // Get prefilled data from intent (if coming from scanner)
        if (getIntent() != null) {
            etName.setText(getIntent().getStringExtra("name"));
            etBrand.setText(getIntent().getStringExtra("brand"));
            etExpiryDate.setText(getIntent().getStringExtra("expiryDate"));
        }

        btnSave.setOnClickListener(v -> saveProductToFirebase());
    }

    private void saveProductToFirebase() {
        String name = etName.getText().toString().trim();
        String brand = etBrand.getText().toString().trim();
        String expiryDate = etExpiryDate.getText().toString().trim();

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("loggedInUsername", null);

        if (username == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.isEmpty() || expiryDate.isEmpty()) {
            Toast.makeText(this, "Please enter product name and expiry date", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference productRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(username)
                .child("products");

        String productId = productRef.push().getKey();

        if (productId != null) {
            ProductModel product = new ProductModel(name, brand, expiryDate);
            productRef.child(productId).setValue(product)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Product saved successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to save product!", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
