package com.example.expiry_alert;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ExpiryNotificationWorker extends Worker {

    public ExpiryNotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();

        SharedPreferences prefs = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String username = prefs.getString("loggedInUsername", null);

        // ✅ Fetch custom notification day preference (default 2)
        SharedPreferences notifyPrefs = context.getSharedPreferences("NotificationPrefs", Context.MODE_PRIVATE);
        int notifyBeforeDays = notifyPrefs.getInt("notifyBeforeDays", 2);

        if (username == null) return Result.failure();

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(username)
                .child("products");

        // ✅ Wait until Firebase completes
        CountDownLatch latch = new CountDownLatch(1);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ProductModel product = ds.getValue(ProductModel.class);
                    if (product != null) {
                        String expiryDateStr = product.getExpiryDate();
                        long daysLeft = getDaysUntilExpiry(expiryDateStr);
                        if (daysLeft <= notifyBeforeDays && daysLeft >= 0) {
                            String msg = product.getName() + " expires in " + daysLeft + " day(s) on " + expiryDateStr;
                            showNotification("Expiry Alert", msg);
                        }
                    }
                }
                latch.countDown(); // 🔓 allow work to complete
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                latch.countDown(); // 🔓 release even on failure
            }
        });

        try {
            latch.await(10, TimeUnit.SECONDS); // wait max 10s
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Result.success();
    }

    private long getDaysUntilExpiry(String expiryDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date expiry = sdf.parse(expiryDate);
            Date today = new Date();

            long diff = expiry.getTime() - today.getTime();
            return TimeUnit.MILLISECONDS.toDays(diff);
        } catch (ParseException e) {
            return Long.MAX_VALUE;
        }
    }

    private void showNotification(String title, String message) {
        Context context = getApplicationContext();
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("expiry_alerts", "Expiry Alerts",
                    NotificationManager.IMPORTANCE_HIGH);
            NotificationManager sysManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (sysManager.getNotificationChannel("expiry_alerts") == null) {
                sysManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "expiry_alerts")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_calendar)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        manager.notify(new Random().nextInt(), builder.build());
    }
}
