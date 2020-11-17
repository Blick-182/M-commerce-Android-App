 package com.example.blacklight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.blacklight.Models.Orders;
import com.example.blacklight.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MerchantNewOrdersActivity extends AppCompatActivity {

    private Toolbar merchantNewOrdersToolbar;
    private RecyclerView merchantNewOrders_recyclerView;

    private DatabaseReference ordersRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_new_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        merchantNewOrdersToolbar = findViewById(R.id.merchant_new_orders_toolbar);
        setSupportActionBar(merchantNewOrdersToolbar);

        merchantNewOrders_recyclerView = findViewById(R.id.merchant_new_orders_recyclerview);
        merchantNewOrders_recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Orders> options = new FirebaseRecyclerOptions.Builder<Orders>().setQuery(ordersRef, Orders.class).build();

        FirebaseRecyclerAdapter<Orders, OrdersViewHolder> adapter = new FirebaseRecyclerAdapter<Orders, OrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrdersViewHolder ordersViewHolder, int i, @NonNull Orders orders) {
                ordersViewHolder.merchantNewOrdersCustomerName.setText(orders.getFirstName());
                ordersViewHolder.merchantNewOrdersCustomerPhoneNumber.setText(orders.getPhoneNumber());
                ordersViewHolder.merchantNewOrdersPickUpSpot.setText("Delivery spot: " + orders.getPickUpSpot());
                ordersViewHolder.merchantNewOrdersTotalPrice.setText(orders.getTotalPrice());
                ordersViewHolder.merchantNewOrdersDateTime.setText("Ordered at: " + orders.getTime() + " " + orders.getDate());

                ordersViewHolder.showMerchantNewOrdersBtn.setOnClickListener(v -> {

                    String uID = getRef(i).getKey();

                    Intent intent = new Intent(MerchantNewOrdersActivity.this, MerchantNewOrderDetailsActivity.class);
                    intent.putExtra("uid", uID);
                    startActivity(intent);
                });

                ordersViewHolder.itemView.setOnClickListener(v -> {
                    CharSequence options1[] = new CharSequence[]{
                            "Yes",
                            "No"
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(MerchantNewOrdersActivity.this);
                    builder.setTitle("Has this ordered been shipped?");
                    builder.setItems(options1, (dialog, which) -> {
                        if (which == 0){
                            String uID = getRef(i).getKey();

                            removeOrder(uID);
                        }
                        else {

                            finish();
                        }
                    });
                    builder.show();
                });
            }

            @NonNull
            @Override
            public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.merchant_new_orders_layout, parent, false);
                return new OrdersViewHolder(view);
            }
        };
        merchantNewOrders_recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    public static class OrdersViewHolder extends RecyclerView.ViewHolder{

        public TextView merchantNewOrdersCustomerName, merchantNewOrdersCustomerPhoneNumber, merchantNewOrdersPickUpSpot, merchantNewOrdersTotalPrice, merchantNewOrdersDateTime;
        public Button showMerchantNewOrdersBtn;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            merchantNewOrdersCustomerName = itemView.findViewById(R.id.merchant_new_order_customer_name);
            merchantNewOrdersCustomerPhoneNumber = itemView.findViewById(R.id.merchant_new_order_customer_phone_number);
            merchantNewOrdersPickUpSpot = itemView.findViewById(R.id.merchant_new_order_customer_pickup_spot);
            merchantNewOrdersTotalPrice = itemView.findViewById(R.id.merchant_new_order_customer_order_total_price);
            merchantNewOrdersDateTime = itemView.findViewById(R.id.merchant_new_order_customer_ordered_at);
            showMerchantNewOrdersBtn = itemView.findViewById(R.id.show_order_details_button);
        }
    }

    private void removeOrder(String uID) {

        ordersRef.child(uID).removeValue();
    }

    public void redirectToMerchantStoreActivity(View view) {
        Intent intent = new Intent(MerchantNewOrdersActivity.this, MerchantStoreActivity.class);
        startActivity(intent);
        finish();
    }
}