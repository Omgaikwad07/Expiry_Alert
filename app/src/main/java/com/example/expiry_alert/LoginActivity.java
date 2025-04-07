//package com.example.expiry_alert;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//
//public class LoginActivity extends AppCompatActivity {
//
//    EditText loginUsername, loginPassword;
//    Button loginButton;
//    TextView signupRedirectText;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        // Initialize UI components
//        loginUsername = findViewById(R.id.login_username);
//        loginPassword = findViewById(R.id.login_password);
//        loginButton = findViewById(R.id.login_button);
//        signupRedirectText = findViewById(R.id.signupRedirectText);
//
//        // ✅ Check if user is already logged in
//        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
//        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
//
//        if (isLoggedIn) {
//            // If already logged in, go to MainActivity
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();  // Prevent user from going back to login screen
//        }
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!validateUsername() | !validatePassword()) {
//                    return;
//                } else {
//                    checkUser();
//                }
//            }
//        });
//
//        signupRedirectText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LoginActivity.this, SighupActivity.class);
//                startActivity(intent);
//                finish(); // Prevent going back to login
//            }
//        });
//    }
//
//    // ✅ Validate Username
//    public boolean validateUsername() {
//        String val = loginUsername.getText().toString();
//        if (val.isEmpty()) {
//            loginUsername.setError("Username cannot be empty");
//            return false;
//        } else {
//            loginUsername.setError(null);
//            return true;
//        }
//    }
//
//    // ✅ Validate Password
//    public boolean validatePassword() {
//        String val = loginPassword.getText().toString();
//        if (val.isEmpty()) {
//            loginPassword.setError("Password cannot be empty");
//            return false;
//        } else {
//            loginPassword.setError(null);
//            return true;
//        }
//    }
//
//    // ✅ Check User in Firebase Database
//    public void checkUser() {
//        String userUsername = loginUsername.getText().toString().trim();
//        String userPassword = loginPassword.getText().toString().trim();
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
//        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);
//
//        checkUserDatabase.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);
//
//                    if (passwordFromDB.equals(userPassword)) {
//                        // ✅ Save login state in SharedPreferences
//                        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putBoolean("isLoggedIn", true);
//                        editor.putString("loggedInUsername", userUsername);
//                        editor.apply();
//
//                        // ✅ Redirect to MainActivity
//                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish(); // Prevent going back to login
//                    } else {
//                        loginPassword.setError("Invalid Credentials");
//                        loginPassword.requestFocus();
//                    }
//                } else {
//                    loginUsername.setError("User does not exist");
//                    loginUsername.requestFocus();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(LoginActivity.this, "Database Error!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
package com.example.expiry_alert;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView signupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);

        // Auto login if already logged in
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        loginButton.setOnClickListener(view -> {
            if (!validateUsername() | !validatePassword()) return;
            checkUser();
        });

        signupRedirectText.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, SighupActivity.class));
            finish();
        });
    }

    public boolean validateUsername() {
        String val = loginUsername.getText().toString();
        if (val.isEmpty()) {
            loginUsername.setError("Username cannot be empty");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    public boolean validatePassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot userSnapshot = null;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        userSnapshot = ds;
                        break;
                    }

                    if (userSnapshot != null) {
                        String passwordFromDB = userSnapshot.child("password").getValue(String.class);
                        if (passwordFromDB != null && passwordFromDB.equals(userPassword)) {
                            SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.putString("loggedInUsername", userUsername);
                            editor.apply();

                            Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            loginPassword.setError("Invalid credentials");
                            loginPassword.requestFocus();
                        }
                    }
                } else {
                    loginUsername.setError("User does not exist");
                    loginUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Database Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

