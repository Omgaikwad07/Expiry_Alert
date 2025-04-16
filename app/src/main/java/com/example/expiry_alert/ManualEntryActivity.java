////////package com.example.expiry_alert;
////////
////////import android.os.Bundle;
////////import android.widget.Button;
////////import android.widget.EditText;
////////import android.widget.Toast;
////////
////////import androidx.appcompat.app.AppCompatActivity;
////////
////////public class ManualEntryActivity extends AppCompatActivity {
////////
////////    EditText editName, editBrand, editExpiry;
////////    Button btnSave;
////////
////////    @Override
////////    protected void onCreate(Bundle savedInstanceState) {
////////        super.onCreate(savedInstanceState);
////////        setContentView(R.layout.activity_manual_entry);
////////
////////        editName = findViewById(R.id.editProductName);
////////        editBrand = findViewById(R.id.editBrandName);
////////        editExpiry = findViewById(R.id.editExpiryDate);
////////        btnSave = findViewById(R.id.btnSave);
////////
////////        // Get data from intent
////////        String name = getIntent().getStringExtra("name");
////////        String brand = getIntent().getStringExtra("brand");
////////        String expiryDate = getIntent().getStringExtra("expiryDate");
////////
////////        // Set prefilled values
////////        editName.setText(name);
////////        editBrand.setText(brand);
////////        editExpiry.setText(expiryDate);
////////
////////        btnSave.setOnClickListener(v -> {
////////            // You can later add Firebase saving logic here
////////            String productName = editName.getText().toString().trim();
////////            String productBrand = editBrand.getText().toString().trim();
////////            String productExpiry = editExpiry.getText().toString().trim();
////////
////////            if (!productName.isEmpty() && !productExpiry.isEmpty()) {
////////                Toast.makeText(this, "Product Saved Locally (Firebase code pending)", Toast.LENGTH_SHORT).show();
////////                finish();
////////            } else {
////////                Toast.makeText(this, "Name and Expiry required!", Toast.LENGTH_SHORT).show();
////////            }
////////        });
////////    }
////////}
//////package com.example.expiry_alert;
//////
//////import android.content.SharedPreferences;
//////import android.os.Bundle;
//////import android.widget.Button;
//////import android.widget.EditText;
//////import android.widget.Toast;
//////
//////import androidx.appcompat.app.AppCompatActivity;
//////
//////import com.google.firebase.database.DatabaseReference;
//////import com.google.firebase.database.FirebaseDatabase;
//////
//////public class ManualEntryActivity extends AppCompatActivity {
//////
//////    EditText etName, etBrand, etExpiryDate;
//////    Button btnSave;
//////
//////    @Override
//////    protected void onCreate(Bundle savedInstanceState) {
//////        super.onCreate(savedInstanceState);
//////        setContentView(R.layout.activity_manual_entry);
//////
//////        etName = findViewById(R.id.editProductName);
//////        etBrand = findViewById(R.id.editBrandName);
//////        etExpiryDate = findViewById(R.id.editExpiryDate);
//////        btnSave = findViewById(R.id.btnSave);
//////
//////        // Get prefilled data from intent (if coming from scanner)
//////        if (getIntent() != null) {
//////            etName.setText(getIntent().getStringExtra("name"));
//////            etBrand.setText(getIntent().getStringExtra("brand"));
//////            etExpiryDate.setText(getIntent().getStringExtra("expiryDate"));
//////        }
//////
//////        btnSave.setOnClickListener(v -> saveProductToFirebase());
//////    }
//////
//////    private void saveProductToFirebase() {
//////        String name = etName.getText().toString().trim();
//////        String brand = etBrand.getText().toString().trim();
//////        String expiryDate = etExpiryDate.getText().toString().trim();
//////
//////        // ✅ Get logged-in username from SharedPreferences
//////        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
//////        String username = sharedPreferences.getString("loggedInUsername", null);
//////
//////        if (username == null) {
//////            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
//////            return;
//////        }
//////
//////        // ✅ Validate input
//////        if (name.isEmpty() || brand.isEmpty() || expiryDate.isEmpty()) {
//////            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
//////            return;
//////        }
//////
//////        // ✅ Save product under user in Firebase
//////        DatabaseReference productRef = FirebaseDatabase.getInstance()
//////                .getReference("users")
//////                .child(username)
//////                .child("products");
//////
//////        String productId = productRef.push().getKey();
//////
//////        if (productId != null) {
//////            ProductModel product = new ProductModel(name, brand, expiryDate);
//////            productRef.child(productId).setValue(product)
//////                    .addOnSuccessListener(aVoid -> {
//////                        Toast.makeText(this, "Product saved successfully!", Toast.LENGTH_SHORT).show();
//////                        finish(); // Close activity
//////                    })
//////                    .addOnFailureListener(e -> {
//////                        Toast.makeText(this, "Failed to save product!", Toast.LENGTH_SHORT).show();
//////                    });
//////        }
//////    }
//////}
////package com.example.expiry_alert;
////
////import android.content.SharedPreferences;
////import android.os.Bundle;
////import android.widget.Button;
////import android.widget.EditText;
////import android.widget.Toast;
////
////import androidx.appcompat.app.AppCompatActivity;
////
////import com.google.firebase.database.DatabaseReference;
////import com.google.firebase.database.FirebaseDatabase;
////
////public class ManualEntryActivity extends AppCompatActivity {
////
////    EditText etName, etBrand, etExpiryDate;
////    Button btnSave;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_manual_entry);
////
////        etName = findViewById(R.id.editProductName);
////        etBrand = findViewById(R.id.editBrandName);
////        etExpiryDate = findViewById(R.id.editExpiryDate);
////        btnSave = findViewById(R.id.btnSave);
////
////        // Get prefilled data from intent (if coming from scanner)
////        if (getIntent() != null) {
////            etName.setText(getIntent().getStringExtra("name"));
////            etBrand.setText(getIntent().getStringExtra("brand"));
////            etExpiryDate.setText(getIntent().getStringExtra("expiryDate"));
////        }
////
////        btnSave.setOnClickListener(v -> saveProductToFirebase());
////    }
////
////    private void saveProductToFirebase() {
////        String name = etName.getText().toString().trim();
////        String brand = etBrand.getText().toString().trim();
////        String expiryDate = etExpiryDate.getText().toString().trim();
////
////        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
////        String username = sharedPreferences.getString("loggedInUsername", null);
////
////        if (username == null) {
////            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
////            return;
////        }
////
////        if (name.isEmpty() || expiryDate.isEmpty()) {
////            Toast.makeText(this, "Please enter product name and expiry date", Toast.LENGTH_SHORT).show();
////            return;
////        }
////
////        DatabaseReference productRef = FirebaseDatabase.getInstance()
////                .getReference("users")
////                .child(username)
////                .child("products");
////
////        String productId = productRef.push().getKey();
////
////        if (productId != null) {
////            ProductModel product = new ProductModel(name, brand, expiryDate);
////            productRef.child(productId).setValue(product)
////                    .addOnSuccessListener(aVoid -> {
////                        Toast.makeText(this, "Product saved successfully!", Toast.LENGTH_SHORT).show();
////                        finish();
////                    })
////                    .addOnFailureListener(e -> {
////                        Toast.makeText(this, "Failed to save product!", Toast.LENGTH_SHORT).show();
////                    });
////        }
////    }
////}
//
////duplicate entry in firebase
////package com.example.expiry_alert;
////
////import android.content.SharedPreferences;
////import android.os.Bundle;
////import android.text.TextUtils;
////import android.widget.EditText;
////import android.widget.Toast;
////import com.google.android.material.button.MaterialButton;
////import androidx.appcompat.app.AppCompatActivity;
////
////import com.google.firebase.database.DatabaseReference;
////import com.google.firebase.database.FirebaseDatabase;
////
////public class ManualEntryActivity extends AppCompatActivity {
////
////    EditText etName, etBrand, etExpiryDate;
////    MaterialButton btnSave;
////    String productId; // store product ID for updating
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_manual_entry);
////
////        etName = findViewById(R.id.editProductName);
////        etBrand = findViewById(R.id.editBrandName);
////        etExpiryDate = findViewById(R.id.editExpiryDate);
////        btnSave = findViewById(R.id.btnSave);
////
////        // Get data from intent
////        productId = getIntent().getStringExtra("productId");
////        String name = getIntent().getStringExtra("productName");
////        String brand = getIntent().getStringExtra("brand");
////        String expiryDate = getIntent().getStringExtra("expiryDate");
////
////        etName.setText(getIntent().getStringExtra("name"));
////        etBrand.setText(getIntent().getStringExtra("brand"));
////        etExpiryDate.setText(getIntent().getStringExtra("expiryDate"));
////
////        btnSave.setOnClickListener(v -> saveProduct());
////    }
////
////    private void saveProduct() {
////        String name = etName.getText().toString().trim();
////        String brand = etBrand.getText().toString().trim();
////        String expiryDate = etExpiryDate.getText().toString().trim();
////
////        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(brand) || TextUtils.isEmpty(expiryDate)) {
////            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
////            return;
////        }
////
////        // Get username
////        SharedPreferences prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
////        String username = prefs.getString("loggedInUsername", null);
////
////        if (username == null) {
////            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
////            return;
////        }
////
////        DatabaseReference ref = FirebaseDatabase.getInstance()
////                .getReference("users")
////                .child(username)
////                .child("products");
////
////        // If productId is null, create new, else update existing
////        if (productId == null) {
////            productId = ref.push().getKey(); // New product
////        }
////
////        ProductModel product = new ProductModel(productId, name, brand, expiryDate);
////
////        ref.child(productId).setValue(product)
////                .addOnSuccessListener(aVoid -> {
////                    Toast.makeText(this, "Product saved!", Toast.LENGTH_SHORT).show();
////                    finish();
////                })
////                .addOnFailureListener(e -> {
////                    Toast.makeText(this, "Failed to save product", Toast.LENGTH_SHORT).show();
////                });
////    }
////}
//package com.example.expiry_alert;
//
//import android.app.DatePickerDialog;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.Calendar;
//
//public class ManualEntryActivity extends AppCompatActivity {
//
//    EditText etName, etBrand, etExpiryDate;
//    TextView headerText;
//
//    Button btnSave;
//    String productId;
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
//        TextView headerTitle = findViewById(R.id.headerTitle);
//
//        if (getIntent() != null && getIntent().hasExtra("productId")) {
//            headerTitle.setText("Edit Product");
//        } else {
//            headerTitle.setText("Add Product");
//        }
//
//
//        // Show DatePicker on expiry click
//        etExpiryDate.setOnClickListener(v -> showDatePicker());
//
//        // Get intent data
//        if (getIntent() != null) {
//            productId = getIntent().getStringExtra("productId");
//            etName.setText(getIntent().getStringExtra("name"));
//            etBrand.setText(getIntent().getStringExtra("brand"));
//            etExpiryDate.setText(getIntent().getStringExtra("expiryDate"));
//        }
//
//        btnSave.setOnClickListener(v -> saveOrUpdateProduct());
//    }
//
//    private void showDatePicker() {
//        final Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog dialog = new DatePickerDialog(this,
//                (view, y, m, d) -> {
//                    String formatted = String.format("%04d-%02d-%02d", y, m + 1, d);
//                    etExpiryDate.setText(formatted);
//                }, year, month, day);
//        dialog.show();
//    }
//
//    private void saveOrUpdateProduct() {
//        String name = etName.getText().toString().trim();
//        String brand = etBrand.getText().toString().trim();
//        String expiry = etExpiryDate.getText().toString().trim();
//
//        if (name.isEmpty() || brand.isEmpty() || expiry.isEmpty()) {
//            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
//        String username = sharedPreferences.getString("loggedInUsername", null);
//
//        if (username == null) {
//            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        DatabaseReference ref = FirebaseDatabase.getInstance()
//                .getReference("users")
//                .child(username)
//                .child("products");
//
//        // ✅ If it's an update
//        if (productId != null && !productId.isEmpty()) {
//            ProductModel updatedProduct = new ProductModel(productId, name, brand, expiry);
//            ref.child(productId).setValue(updatedProduct)
//                    .addOnSuccessListener(unused -> {
//                        Toast.makeText(this, "Product updated!", Toast.LENGTH_SHORT).show();
//                        finish();
//                    })
//                    .addOnFailureListener(e -> {
//                        Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    });
//        } else {
//            // ✅ If it's a new product
//            String newProductId = ref.push().getKey();
//            ProductModel newProduct = new ProductModel(newProductId, name, brand, expiry);
//
//            ref.child(newProductId).setValue(newProduct)
//                    .addOnSuccessListener(unused -> {
//                        Toast.makeText(this, "Product added!", Toast.LENGTH_SHORT).show();
//                        finish();
//                    })
//                    .addOnFailureListener(e -> {
//                        Toast.makeText(this, "Save failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    });
//        }
//    }
//}
//
//
package com.example.expiry_alert;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ManualEntryActivity extends AppCompatActivity {

    EditText etName, etBrand, etExpiryDate, etCategory;
    TextView headerText;
    Button btnSave;
    String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

        etName = findViewById(R.id.editProductName);
        etBrand = findViewById(R.id.editBrandName);
        etExpiryDate = findViewById(R.id.editExpiryDate);
        etCategory = findViewById(R.id.editCategory); // 🆕 New field
        btnSave = findViewById(R.id.btnSave);
        TextView headerTitle = findViewById(R.id.headerTitle);

        // Header title: Add or Edit
        if (getIntent() != null && getIntent().hasExtra("productId")) {
            headerTitle.setText("Edit Product");
        } else {
            headerTitle.setText("Add Product");
        }

        // Show DatePicker on expiry field
        etExpiryDate.setOnClickListener(v -> showDatePicker());

        // Get data from intent
        if (getIntent() != null) {
            productId = getIntent().getStringExtra("productId");
            etName.setText(getIntent().getStringExtra("name"));
            etBrand.setText(getIntent().getStringExtra("brand"));
            etExpiryDate.setText(getIntent().getStringExtra("expiryDate"));
            etCategory.setText(getIntent().getStringExtra("category")); // 🆕
        }

        btnSave.setOnClickListener(v -> saveOrUpdateProduct());
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, y, m, d) -> {
                    String formatted = String.format("%04d-%02d-%02d", y, m + 1, d);
                    etExpiryDate.setText(formatted);
                }, year, month, day);
        dialog.show();
    }

    private void saveOrUpdateProduct() {
        String name = etName.getText().toString().trim();
        String brand = etBrand.getText().toString().trim();
        String expiry = etExpiryDate.getText().toString().trim();
        String category = etCategory.getText().toString().trim();

        if (name.isEmpty() || brand.isEmpty() || expiry.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("loggedInUsername", null);

        if (username == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(username)
                .child("products");

        if (productId != null && !productId.isEmpty()) {
            // 🔁 Update product
            ProductModel updatedProduct = new ProductModel(productId, name, brand, expiry, category);
            ref.child(productId).setValue(updatedProduct)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Product updated!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // ➕ Add new product
            String newProductId = ref.push().getKey();
            ProductModel newProduct = new ProductModel(newProductId, name, brand, expiry, category);

            ref.child(newProductId).setValue(newProduct)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Product added!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Save failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}

