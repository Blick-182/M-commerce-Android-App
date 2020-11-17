package com.example.blacklight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.paperdb.Paper;

public class MerchantStoreActivity extends AppCompatActivity {

    private Toolbar merchantToolBar;
    private FloatingActionButton merchantStoreFab;
    private Button merchantStoreNewOrdersBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_store);

        merchantToolBar = findViewById(R.id.merchant_store_toolbar);
        merchantStoreFab = findViewById(R.id.add_new_product_fab_button);
        merchantStoreNewOrdersBtn = findViewById(R.id.merchant_store_orders_button);

        setSupportActionBar(merchantToolBar);
    }

    public void sendToChooseCategory(View view) {
        Intent intent = new Intent(MerchantStoreActivity.this, MerchantChooseInventoryCategoryActivity.class);
        startActivity(intent);
    }

    public void sendToNewOrders(View view) {
        Intent intent = new Intent(MerchantStoreActivity.this, MerchantNewOrdersActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.merchant_store_appbar_menu, menu);
        return true;
    }

    public void signOutMerchant(MenuItem item) {

        Intent intent = new Intent(MerchantStoreActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}