package com.example.expiry_alert;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import org.json.JSONException;
import org.json.JSONObject;

public class MyCaptureActivity extends AppCompatActivity {

    private DecoratedBarcodeView barcodeView;
    private Button addDetailsButton;
    private boolean isScanned = false; // Prevent multiple scans

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_scanner_layout);

        barcodeView = findViewById(R.id.barcode_scanner);
        addDetailsButton = findViewById(R.id.add_details_button);

        barcodeView.resume();  // ✅ Scanner starts immediately
        barcodeView.decodeContinuous(callback);

        // ✅ "Add Details Manually" button now correctly opens Manual Entry
        addDetailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyCaptureActivity.this, ManualEntryActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (!isScanned && result.getText() != null) {
                isScanned = true;
                barcodeView.pause(); // ✅ Stop scanning after success
                fetchProductDetails(result.getText()); // ✅ Fetch product details via API
            }
        }
    };

    private void fetchProductDetails(String barcode) {
        String url = "https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject product = response.getJSONObject("product");
                        String name = product.optString("product_name", "Unknown Product");
                        String category = product.optJSONArray("categories_tags") != null ?
                                product.getJSONArray("categories_tags").optString(0, "Uncategorized") : "Uncategorized";

                        // ✅ Redirect to Manual Entry Page with product details
                        Intent intent = new Intent(MyCaptureActivity.this, ManualEntryActivity.class);
                        intent.putExtra("SCANNED_BARCODE", barcode);
                        intent.putExtra("PRODUCT_NAME", name);
                        intent.putExtra("PRODUCT_CATEGORY", category);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        Toast.makeText(MyCaptureActivity.this, "Product not found, enter manually.", Toast.LENGTH_SHORT).show();
                        redirectToManualEntry(barcode);
                    }
                },
                error -> {
                    Toast.makeText(MyCaptureActivity.this, "API Error, enter manually.", Toast.LENGTH_SHORT).show();
                    redirectToManualEntry(barcode);
                });

        queue.add(jsonObjectRequest);
    }

    private void redirectToManualEntry(String barcode) {
        Intent intent = new Intent(MyCaptureActivity.this, ManualEntryActivity.class);
        intent.putExtra("SCANNED_BARCODE", barcode);
        startActivity(intent);
        finish();
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
}
