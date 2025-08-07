package com.sobeeKinun.addminapp.Product;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.sobeeKinun.addminapp.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ProductModel> productList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ProductModel product);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ProductAdapter(List<ProductModel> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel product = productList.get(position);

        holder.textName.setText("Product Name: "+product.getName());
        holder.ProductId.setText("Product ID: "+product.getId());


        holder.itemView.setOnClickListener(v -> {

            if (listener != null) listener.onItemClick(product);
        });
    }

    @Override
    public int getItemCount() {
        Log.d("DEBUGfff", "Product List Size: " + productList.size());
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textName,ProductId;
        MaterialCardView NextActivity;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textProductName);
            ProductId = itemView.findViewById(R.id.tvProductId);
            NextActivity = itemView.findViewById(R.id.NextActivity);
        }
    }
}
