package com.example.blacklight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.blacklight.Models.Products;
import com.example.blacklight.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView productDetailsImage;
    private TextView productDetailsName, productDetailsPrice, productDetailsDescription;
    private ElegantNumberButton productDetailsCounterBtn;
   // private FloatingActionButton productDetailsFAB;
    private Button productDetailsBtn;

    private String productID = "", state = "normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");

        productDetailsImage = findViewById(R.id.product_details_image);
        productDetailsName = findViewById(R.id.product_details_name);
        productDetailsPrice = findViewById(R.id.product_details_price);
        productDetailsCounterBtn = findViewById(R.id.product_details_counter_button);
        productDetailsDescription = findViewById(R.id.product_details_description);
      //  productDetailsFAB = findViewById(R.id.product_details_fab_button);
        productDetailsBtn = findViewById(R.id.product_details_add_to_cart_button);
        
        getProductDetails(productID);
    }

    private void getProductDetails(String productID) {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Products products = snapshot.getValue(Products.class);

                    productDetailsName.setText(products.getName());
                    productDetailsDescription.setText(products.getDescription());
                    productDetailsPrice.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(productDetailsImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkOrderState();
    }

    public void sendToCart(View view) {

        if (state.equals("Order placed") || state.equals("Order delivered")){

            Snackbar.make(view, "Continue shopping after your pending order is delivered", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else {
            addToCart();
        }
    }

    private void addToCart() {

        String saveCurrentTime, saveCurrentDate;

        Calendar callForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("name", productDetailsName.getText().toString());
        cartMap.put("price", productDetailsPrice.getText().toString());
        cartMap.put("quantity", productDetailsCounterBtn.getNumber());
        cartMap.put("discount", "");
       // cartMap.put("image", productDetailsImage);
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);

        cartRef.child("Customer View").child(Prevalent.currentOnlineCustomer.getPhoneNumber())
                .child("Products added in Cart").child(productID).updateChildren(cartMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        cartRef.child("Merchant View").child("Products in Cart").child(productID).updateChildren(cartMap)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()){

                                        Toast.makeText(this, "Added to Cart", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    }
                });
    }

    private void checkOrderState(){

        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineCustomer.getPhoneNumber());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String deliveryState = snapshot.child("state").getValue().toString();
                    if (deliveryState.equals("delivered")){
                        state = "Order delivered";
                    }
                    else if (deliveryState.equals("not delivered")){
                        state = "Order placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductDetailsActivity.this, "Unexpected error, try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }
}