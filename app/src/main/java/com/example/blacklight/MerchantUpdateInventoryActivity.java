package com.example.blacklight;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MerchantUpdateInventoryActivity extends AppCompatActivity {

    private String productID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_update_inventory);

        productID = getIntent().getStringExtra("pid");


    }
}