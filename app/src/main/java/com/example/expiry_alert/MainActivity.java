////package com.example.expiry_alert;
////
////import android.os.Bundle;
////import androidx.annotation.NonNull;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.fragment.app.Fragment;
////import com.google.android.material.bottomnavigation.BottomNavigationView;
////import com.google.android.material.floatingactionbutton.FloatingActionButton;
////import com.journeyapps.barcodescanner.ScanContract;
////import com.journeyapps.barcodescanner.ScanOptions;
////import android.view.MenuItem;
////
////public class MainActivity extends AppCompatActivity {
////
////    BottomNavigationView bottomNavigationView;
////    FloatingActionButton fab;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
////
////        bottomNavigationView = findViewById(R.id.bottomNavigationView);
////        fab = findViewById(R.id.fab);
////
////        // Set default fragment to Home
////        if (savedInstanceState == null) {
////            getSupportFragmentManager().beginTransaction()
////                    .replace(R.id.fragment_container, new HomeFragment())
////                    .commit();
////        }
////
////        // Handle Bottom Navigation Clicks
////        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
////            @Override
////            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
////                Fragment selectedFragment = null;
////
////                if (item.getItemId() == R.id.navigation_home) {
////                    selectedFragment = new HomeFragment();
////                } else if (item.getItemId() == R.id.navigation_search) {
////                    selectedFragment = new SearchFragment();
////                } else if (item.getItemId() == R.id.navigation_list) {
////                    selectedFragment = new ListFragment();
////                } else if (item.getItemId() == R.id.navigation_settings) {
////                    selectedFragment = new SettingFragment();
////                }
////
////                if (selectedFragment != null) {
////                    getSupportFragmentManager().beginTransaction()
////                            .replace(R.id.fragment_container, selectedFragment)
////                            .commit();
////                }
////                return true;
////            }
////        });
////
////        // Handle Floating Action Button (FAB) Click - Open Scanner
////        fab.setOnClickListener(v -> {
////            openScanner();
////        });
////    }
////
////    // Function to Open Barcode Scanner
////    private void openScanner() {
////        ScanOptions options = new ScanOptions();
////        options.setPrompt("Scan a barcode");
////        options.setBeepEnabled(true);
////        options.setOrientationLocked(true);
////        options.setCaptureActivity(ScannerActivity.class);  // ✅ Use custom capture activity
////        barcodeLauncher.launch(options);
////    }
////
////
////
////    // Handling the Scanned Barcode Result
////    private final androidx.activity.result.ActivityResultLauncher<ScanOptions> barcodeLauncher =
////            registerForActivityResult(new ScanContract(), result -> {
////                if (result.getContents() != null) {
////                    String scannedData = result.getContents();
////                    System.out.println("Scanned Data: " + scannedData);
////                    // TODO: Process the scanned data (store in database, display in UI, etc.)
////                }
////            });
////}/
//*****Notification basic running
//package com.example.expiry_alert;
//
//import android.os.Bundle;
//import android.view.MenuItem;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import androidx.work.PeriodicWorkRequest;
//import androidx.work.WorkManager;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.journeyapps.barcodescanner.ScanContract;
//import com.journeyapps.barcodescanner.ScanOptions;
//
//import java.util.concurrent.TimeUnit;
//
//public class MainActivity extends AppCompatActivity {
//
//    BottomNavigationView bottomNavigationView;
//    FloatingActionButton fab;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        fab = findViewById(R.id.fab);
//
//        // ✅ Step 2: Schedule expiry check notification daily using WorkManager
//        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
//                ExpiryNotificationWorker.class,
//                1, TimeUnit.DAYS)
//                .build();
//        WorkManager.getInstance(this).enqueue(workRequest);
//
//        // Load HomeFragment by default
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, new HomeFragment())
//                    .commit();
//        }
//
//        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment selectedFragment = null;
//
//                if (item.getItemId() == R.id.navigation_home) {
//                    selectedFragment = new HomeFragment();
//                } else if (item.getItemId() == R.id.navigation_search) {
//                    selectedFragment = new SearchFragment();
//                } else if (item.getItemId() == R.id.navigation_list) {
//                    selectedFragment = new ListFragment();
//                } else if (item.getItemId() == R.id.navigation_settings) {
//                    selectedFragment = new SettingFragment();
//                }
//
//                if (selectedFragment != null) {
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.fragment_container, selectedFragment)
//                            .commit();
//                }
//                return true;
//            }
//        });
//
//        fab.setOnClickListener(v -> openScanner());
//    }
//
//    private void openScanner() {
//        ScanOptions options = new ScanOptions();
//        options.setPrompt("Scan a barcode");
//        options.setBeepEnabled(true);
//        options.setOrientationLocked(true);
//        options.setCaptureActivity(ScannerActivity.class);
//        barcodeLauncher.launch(options);
//    }
//
//    private final androidx.activity.result.ActivityResultLauncher<ScanOptions> barcodeLauncher =
//            registerForActivityResult(new ScanContract(), result -> {
//                if (result.getContents() != null) {
//                    String scannedData = result.getContents();
//                    System.out.println("Scanned Data: " + scannedData);
//                    // You can also handle the result or start ScannerActivity here
//                }
//            });
//}
//*** notification daily 8 am
package com.example.expiry_alert;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.ExistingWorkPolicy;
import androidx.work.WorkRequest;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);

        // ✅ Schedule daily worker at 8 AM
        scheduleDailyNotificationWorkerAt8AM();

        // Load HomeFragment by default
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.navigation_home) {
                    selectedFragment = new HomeFragment();
                } else if (item.getItemId() == R.id.navigation_search) {
                    selectedFragment = new SearchFragment();
                } else if (item.getItemId() == R.id.navigation_list) {
                    selectedFragment = new ListFragment();
                } else if (item.getItemId() == R.id.navigation_settings) {
                    selectedFragment = new SettingFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }
                return true;
            }
        });

        fab.setOnClickListener(v -> openScanner());
    }

    // 📅 Function to schedule the worker to run daily at 8 AM
    private void scheduleDailyNotificationWorkerAt8AM() {
        Calendar calendar = Calendar.getInstance();
        long currentTime = System.currentTimeMillis();

        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long delay = calendar.getTimeInMillis() - currentTime;
        if (delay < 0) {
            delay += TimeUnit.DAYS.toMillis(1); // Schedule for tomorrow
        }

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(
                ExpiryNotificationWorker.class,
                1, TimeUnit.DAYS
        )
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .addTag("expiry_alert_worker")
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "expiryCheckWork",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
        );

    }

    private void openScanner() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan a barcode");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(ScannerActivity.class);
        barcodeLauncher.launch(options);
    }

    private final androidx.activity.result.ActivityResultLauncher<ScanOptions> barcodeLauncher =
            registerForActivityResult(new ScanContract(), result -> {
                if (result.getContents() != null) {
                    String scannedData = result.getContents();
                    System.out.println("Scanned Data: " + scannedData);
                    // You can also handle the result or start ScannerActivity here
                }
            });
}


