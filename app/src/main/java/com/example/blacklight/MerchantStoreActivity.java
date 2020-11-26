package com.example.blacklight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blacklight.Models.Products;
import com.example.blacklight.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class MerchantStoreActivity extends AppCompatActivity {

    private Toolbar merchantToolBar;
    private FloatingActionButton merchantStoreFab;
   // private Button merchantStoreNewOrdersBtn;
   private RecyclerView merchantStore_recyclerView;

    private DatabaseReference ProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_store);

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        merchantToolBar = findViewById(R.id.merchant_store_toolbar);
        merchantStoreFab = findViewById(R.id.add_new_product_fab_button);
       // merchantStoreNewOrdersBtn = findViewById(R.id.merchant_store_orders_button);

        setSupportActionBar(merchantToolBar);

        merchantStore_recyclerView = findViewById(R.id.merchant_store_recyclerview);
        merchantStore_recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(ProductsRef, Products.class).build();

        FirebaseRecyclerAdapter<Products, InventoryViewHolder> adapter = new FirebaseRecyclerAdapter<Products, InventoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull InventoryViewHolder inventoryViewHolder, int i, @NonNull Products products) {

                Picasso.get().load(products.getImage()).into(inventoryViewHolder.merchantInventoryImage);
                inventoryViewHolder.merchantInventoryName.setText(products.getName());
                inventoryViewHolder.merchantInventoryPrice.setText("Ksh " + products.getPrice());

                inventoryViewHolder.merchantInventoryMoreOptions.setOnClickListener(v -> {
                    CharSequence options1[] = new CharSequence[]{
                            "Update inventory",
                            "Delete inventory"
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(MerchantStoreActivity.this);
                    builder.setTitle("What do you want to do?");
                    builder.setItems(options1, (dialog, which) -> {
                        if (which == 0){

                            Intent intent = new Intent(MerchantStoreActivity.this, MerchantUpdateInventoryActivity.class);
                            intent.putExtra("pid", products.getPid());
                            startActivity(intent);
                        }
                        else {

                            String uID = getRef(i).getKey();
                            removeInventory(uID);
                        }
                    });
                    builder.show();
                });
            }

            @NonNull
            @Override
            public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.merchant_inventory_items_layout, parent, false);
                return new InventoryViewHolder(view);
            }
        };
        merchantStore_recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public static class InventoryViewHolder extends RecyclerView.ViewHolder{

        public TextView merchantInventoryName, merchantInventoryPrice;
        public ImageView merchantInventoryImage, merchantInventoryMoreOptions;

        public InventoryViewHolder(@NonNull View itemView) {
            super(itemView);

            merchantInventoryName = itemView.findViewById(R.id.inventory_name);
            merchantInventoryPrice = itemView.findViewById(R.id.inventory_price);
            merchantInventoryImage = itemView.findViewById(R.id.inventory_image);
            merchantInventoryMoreOptions = itemView.findViewById(R.id.inventory_more_options);
        }
    }

    private void removeInventory(String uID) {

        ProductsRef.child(uID).removeValue();
    }

    public void sendToChooseCategory(View view) {
        Intent intent = new Intent(MerchantStoreActivity.this, MerchantChooseInventoryCategoryActivity.class);
        startActivity(intent);
    }

   /* public void sendToNewOrders(View view) {
        Intent intent = new Intent(MerchantStoreActivity.this, MerchantNewOrdersActivity.class);
        startActivity(intent);
    } */

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.merchant_store_appbar_menu, menu);
        return true;
    }

    public void showMerchantOrders(MenuItem item) {
        Intent intent = new Intent(MerchantStoreActivity.this, MerchantNewOrdersActivity.class);
        startActivity(intent);
    }

    public void sendToMerchantProfile(MenuItem item) {
        //TO DO: Redirect to merchant profile
    }

    public void signOutMerchant(MenuItem item) {

        Intent intent = new Intent(MerchantStoreActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}