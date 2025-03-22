package com.example.expiry_alert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;

public class MyCaptureActivity extends AppCompatActivity {

    private DecoratedBarcodeView barcodeView;
    private static final int REQUEST_MANUAL_ENTRY = 1; // Unique request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_scanner_layout);

        // Initialize Scanner
        barcodeView = findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(callback); // ✅ Start scanning immediately

        // Initialize "Add Details Manually" Button
        Button addDetailsButton = findViewById(R.id.add_details_button);
        addDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Manual Entry Activity
                Intent intent = new Intent(MyCaptureActivity.this, ManualEntryActivity.class);
                startActivityForResult(intent, REQUEST_MANUAL_ENTRY);
            }
        });
    }

    // Callback for barcode scanning
    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                barcodeView.pause(); // Pause scanner after scan
                Intent intent = new Intent();
                intent.putExtra("SCANNED_DATA", result.getText());
                setResult(RESULT_OK, intent);
                finish(); // Close scanner activity
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume(); // ✅ Ensure scanner restarts after returning from manual entry
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause(); // ✅ Pause scanner when switching screens
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If returning from Manual Entry, restart the scanner
        if (requestCode == REQUEST_MANUAL_ENTRY) {
            barcodeView.resume();  // ✅ Ensure scanner is visible when returning
        }
    }
}
