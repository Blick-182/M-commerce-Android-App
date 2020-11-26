package com.example.blacklight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.blacklight.Models.Cart;
import com.example.blacklight.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MerchantNewOrderDetailsActivity extends AppCompatActivity {

    private Toolbar merchantNewOrderDetailsToolbar;
    private RecyclerView merchantNewOrderDetails_recyclerView;

    private DatabaseReference cartRef;

    private String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_new_order_details);

        userID = getIntent().getStringExtra("uid");

        cartRef = FirebaseDatabase.getInstance().getReference().child("Cart").child("Merchant View").child(userID).child("Products in Cart");

        merchantNewOrderDetailsToolbar = findViewById(R.id.merchant_new_order_details_toolbar);
        setSupportActionBar(merchantNewOrderDetailsToolbar);

        merchantNewOrderDetails_recyclerView = findViewById(R.id.merchant_new_orders_details_recyclerview);
        merchantNewOrderDetails_recyclerView.setLayoutManager(new LinearLayoutManager(this));
       // merchantNewOrderDetails_recyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartRef, Cart.class).build();

        FirebaseRecyclerAdapter<Cart, MerchantCartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, MerchantCartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MerchantCartViewHolder merchantCartViewHolder, int i, @NonNull Cart cart) {

                merchantCartViewHolder.merchantCartProductName.setText(cart.getName());
                merchantCartViewHolder.merchantCartProductQuantity.setText(cart.getQuantity());
                merchantCartViewHolder.merchantCartProductPrice.setText(cart.getPrice());

            }

            @NonNull
            @Override
            public MerchantCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.merchant_cart_items_layout, parent, false);
                return new MerchantCartViewHolder(view);
            }
        };
        merchantNewOrderDetails_recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class MerchantCartViewHolder extends RecyclerView.ViewHolder{

        public TextView merchantCartProductName, merchantCartProductQuantity, merchantCartProductPrice;

        public MerchantCartViewHolder(@NonNull View itemView) {
            super(itemView);

            merchantCartProductName = itemView.findViewById(R.id.merchant_cart_product_name);
            merchantCartProductQuantity = itemView.findViewById(R.id.merchant_cart_product_quantity);
            merchantCartProductPrice = itemView.findViewById(R.id.merchant_cart_product_price);
        }
    }

    public void redirectToDisplayMerchantNewOrders(View view) {
        Intent intent = new Intent(MerchantNewOrderDetailsActivity.this, MerchantNewOrdersActivity.class);
        startActivity(intent);
        finish();
    }
}