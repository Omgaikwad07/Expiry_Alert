package com.example.expiry_alert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SighupActivity extends AppCompatActivity {
    EditText signupName, signupEmail, signupUsername, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;
//newline
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sighup);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.sighup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndSignup();
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ✅ Clear login state before going to LoginActivity
                SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", false); // Mark user as NOT logged in
                editor.apply();

                Intent intent = new Intent(SighupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Prevents going back to signup page
            }
        });

    }

    private void validateAndSignup() {
        String name = signupName.getText().toString().trim();
        String email = signupEmail.getText().toString().trim();
        String username = signupUsername.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();

        // ✅ Check if name is empty
        if (name.isEmpty()) {
            signupName.setError("Name is required");
            signupName.requestFocus();
            return;
        }

        // ✅ Check if email is empty or invalid
        if (email.isEmpty()) {
            signupEmail.setError("Email is required");
            signupEmail.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupEmail.setError("Enter a valid email address");
            signupEmail.requestFocus();
            return;
        }

        // ✅ Check if username is empty
        if (username.isEmpty()) {
            signupUsername.setError("Username is required");
            signupUsername.requestFocus();
            return;
        }

        // ✅ Check if password is empty or less than 6 characters
        if (password.isEmpty()) {
            signupPassword.setError("Password is required");
            signupPassword.requestFocus();
            return;
        } else if (password.length() < 6) {
            signupPassword.setError("Password must be at least 6 characters");
            signupPassword.requestFocus();
            return;
        }

        // ✅ If all validations pass, store data in Firebase
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        HelperClass helperClass = new HelperClass(name, email, username, password);
        reference.child(username).setValue(helperClass);

        // ✅ Save login state in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.apply();

        Toast.makeText(SighupActivity.this, "Signup Successful!", Toast.LENGTH_SHORT).show();

        // ✅ Redirect to MainActivity
        Intent intent = new Intent(SighupActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Prevents going back to signup page
    }
}
