package com.example.blacklight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blacklight.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CheckoutActivity extends AppCompatActivity {

    private Toolbar checkoutToolbar;
    private EditText checkoutFirstName, checkoutLastName, checkoutPhoneNumber, checkoutPickUpSpot;
    private TextView checkoutTotalAmount, checkoutDisplayTotalPrice;
    private Button checkoutBtn;

    private String checkoutTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        checkoutTotalPrice = getIntent().getStringExtra("Total price");

        checkoutToolbar = findViewById(R.id.checkout_toolbar);
        checkoutFirstName = findViewById(R.id.checkout_first_name);
        checkoutLastName = findViewById(R.id.checkout_last_name);
        checkoutPhoneNumber = findViewById(R.id.checkout_phone_number);
        checkoutPickUpSpot = findViewById(R.id.checkout_pick_up_spot);
        checkoutTotalAmount = findViewById(R.id.checkout_total_amount);
        checkoutDisplayTotalPrice = findViewById(R.id.checkout_display_price);
        checkoutBtn = findViewById(R.id.checkout_button);

        checkoutDisplayTotalPrice.setText("Ksh " + String.valueOf(checkoutTotalPrice));

    }

    public void redirectToCartActivity(View view) {
        Intent intent = new Intent(CheckoutActivity.this, CartActivity.class);
        startActivity(intent);
        finish();
    }

    public void finalizeOrder(View view) {
        validateShippingDetails();
    }

    private void validateShippingDetails() {

        if (TextUtils.isEmpty(checkoutFirstName.getText().toString())){
            Toast.makeText(CheckoutActivity.this, "Please provide your first name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(checkoutLastName.getText().toString())){
            Toast.makeText(CheckoutActivity.this, "Please provide your last name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(checkoutPhoneNumber.getText().toString())){
            Toast.makeText(CheckoutActivity.this, "How are we going to contact you?", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(checkoutPickUpSpot.getText().toString())){
            Toast.makeText(CheckoutActivity.this, "But where we'll we deliver your order?", Toast.LENGTH_SHORT).show();
        }
        else {
            confirmOrder();
        }
    }

    private void confirmOrder() {

        String saveCurrentTime, saveCurrentDate;

        Calendar callForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineCustomer.getPhoneNumber());

        final HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("firstName", checkoutFirstName.getText().toString());
        ordersMap.put("lastName", checkoutLastName.getText().toString());
        ordersMap.put("phoneNumber", checkoutPhoneNumber.getText().toString());
        ordersMap.put("pickUpSpot", checkoutPickUpSpot.getText().toString());
        ordersMap.put("totalPrice", checkoutDisplayTotalPrice.getText().toString());
        ordersMap.put("state", "not delivered");
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                FirebaseDatabase.getInstance().getReference().child("Cart").child("Customer View")
                        .child(Prevalent.currentOnlineCustomer.getPhoneNumber()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(CheckoutActivity.this, "Order placed successfully..", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(CheckoutActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });

    }
}