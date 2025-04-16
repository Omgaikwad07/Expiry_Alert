package com.example.expiry_alert;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.util.ArrayList;

public class ViewProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ArrayList<ProductModel> productList;

    private DatabaseReference userProductRef;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_products); // Make sure this XML has a RecyclerView with ID recycler_view

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);

        recyclerView.setAdapter(productAdapter);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userProductRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId).child("products");

        fetchUserProducts();
    }

    private void fetchUserProducts() {
        userProductRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    ProductModel product = productSnapshot.getValue(ProductModel.class);
                    productList.add(product);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error here
            }
        });
    }
}
