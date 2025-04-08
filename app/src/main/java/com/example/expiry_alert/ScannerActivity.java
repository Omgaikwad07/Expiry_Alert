package com.example.expiry_alert;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ScannerActivity extends AppCompatActivity {

    private Button btnEnterManually;
    private DecoratedBarcodeView barcodeView;
    private boolean isScanned = false; // Prevent multiple scans

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_scanner_layout);

        barcodeView = findViewById(R.id.barcode_scanner);
        btnEnterManually = findViewById(R.id.add_details_button);

        btnEnterManually.setOnClickListener(v -> {
            Intent intent = new Intent(ScannerActivity.this, ManualEntryActivity.class);
            startActivity(intent);
        });

        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (!isScanned) {
                    isScanned = true;
                    fetchProductDetails(result.getText());
                }
            }

            @Override
            public void possibleResultPoints(java.util.List<com.google.zxing.ResultPoint> resultPoints) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
        isScanned = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    private void fetchProductDetails(String barcode) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    String apiUrl = "https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json";
                    URL url = new URL(apiUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    return response.toString();
                } catch (Exception e) {
                    Log.e("API_ERROR", "Error fetching product", e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int status = jsonObject.optInt("status", 0);

                        if (status == 1 && jsonObject.has("product")) {
                            JSONObject product = jsonObject.getJSONObject("product");

                            String name = product.optString("product_name", "");
                            String brand = product.optString("brands", "");
                            String expiryDate = product.optString("expiration_date", "");

                            // If name is missing, treat as invalid product
                            if (name.isEmpty()) {
                                showNotFoundDialog();
                                return;
                            }

                            Intent intent = new Intent(ScannerActivity.this, ManualEntryActivity.class);
                            intent.putExtra("name", name);
                            intent.putExtra("brand", brand);
                            intent.putExtra("expiryDate", expiryDate);
                            startActivity(intent);
                            finish();
                        } else {
                            showNotFoundDialog();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showNotFoundDialog();
                    }
                } else {
                    showNotFoundDialog();
                }
            }
        }.execute();
    }

    private void showNotFoundDialog() {
        runOnUiThread(() -> new androidx.appcompat.app.AlertDialog.Builder(ScannerActivity.this)
                .setTitle("Product Not Found")
                .setMessage("This barcode is not available in the database.\nYou can enter details manually.")
                .setPositiveButton("Enter Manually", (dialog, which) -> {
                    Intent intent = new Intent(ScannerActivity.this, ManualEntryActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    isScanned = false; // Allow scanning again
                })
                .show());
    }
}
