package com.example.blacklight.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blacklight.Interface.ItemClickListner;
import com.example.blacklight.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView displayProductName, displayProductPrice, displayProductDescription;
    public ImageView displayProductImage;
    public ItemClickListner listner;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        displayProductImage = itemView.findViewById(R.id.home_product_image);
        displayProductName = itemView.findViewById(R.id.home_product_name);
        displayProductPrice = itemView.findViewById(R.id.home_product_price);
       // displayProductDescription = itemView.findViewById(R.id.home_product_description);
    }

    public void setItemClickListner(ItemClickListner listner){
        this.listner = listner;
    }

    @Override
    public void onClick(View v) {
        listner.onClick(v, getAdapterPosition(), false);
    }
}
