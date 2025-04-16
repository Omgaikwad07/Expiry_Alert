package com.example.expiry_alert;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ArrayList<ProductModel> productList = new ArrayList<>();

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductAdapter(getContext(), productList);

        recyclerView.setAdapter(adapter);

        // ✅ Get logged-in username from SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String username = prefs.getString("loggedInUsername", null);

        if (username == null) {
            Toast.makeText(getContext(), "User not logged in!", Toast.LENGTH_SHORT).show();
            return view;
        }

        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(username)
                .child("products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ProductModel product = dataSnapshot.getValue(ProductModel.class);

                            if (product != null) {
                                // ✅ Set the productId manually from the key
                                product.setProductId(dataSnapshot.getKey());
                                productList.add(product);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
                    }
                });


        return view;
    }
}
