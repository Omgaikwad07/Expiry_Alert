package com.example.expiry_alert;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private List<ProductModel> productList;

    public ProductAdapter(Context context, List<ProductModel> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        ProductModel product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.brand.setText("Brand: " + product.getBrand());
        holder.expiry.setText("Expires: " + product.getExpiryDate());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ManualEntryActivity.class);
            intent.putExtra("productId", product.getProductId());
            intent.putExtra("name", product.getName());
            intent.putExtra("brand", product.getBrand());
            intent.putExtra("expiryDate", product.getExpiryDate());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName, brand, expiry;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.tvProductName);
            brand = itemView.findViewById(R.id.tvBrand);
            expiry = itemView.findViewById(R.id.tvExpiry);
        }
    }
}
