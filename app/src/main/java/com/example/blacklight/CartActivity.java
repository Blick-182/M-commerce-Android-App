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
import android.widget.Toast;

import com.example.blacklight.Models.Cart;
import com.example.blacklight.Prevalent.Prevalent;
import com.example.blacklight.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    private Toolbar cartToolbar;
    private RecyclerView cart_recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView cartTotalAmountPrice, cartDisplayTotalPrice, cartPendingOrder;
    private Button cartCheckoutBtn;

    private int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartToolbar = findViewById(R.id.cart_toolbar);

        cart_recyclerView = findViewById(R.id.cart_recyclerview);
        cart_recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        cart_recyclerView.setLayoutManager(layoutManager);

        cartTotalAmountPrice = findViewById(R.id.cart_total_amount);
        cartDisplayTotalPrice = findViewById(R.id.cart_total_amount_display);
        cartPendingOrder = findViewById(R.id.cart_pending_order);
        cartCheckoutBtn = findViewById(R.id.cart_checkout_button);

        cartPendingOrder.setVisibility(View.INVISIBLE);

        onStart();
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkOrderState();

        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartRef.child("Customer View")
                .child(Prevalent.currentOnlineCustomer.getPhoneNumber()).child("Products added in Cart"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cart) {

                cartViewHolder.cartProductName.setText(cart.getName());
                cartViewHolder.cartProductQuantity.setText(cart.getQuantity());
                cartViewHolder.cartProductPrice.setText(cart.getPrice());

             /*   int oneCartItemTotalPrice = (Integer.valueOf(cart.getPrice())) * Integer.valueOf(cart.getQuantity());
                totalPrice = totalPrice + oneCartItemTotalPrice;

                cartDisplayTotalPrice.setText("Ksh " + String.valueOf(totalPrice)); */

                cartViewHolder.itemView.setOnClickListener(v -> {
                    CharSequence charSequence[] = new CharSequence[]{
                            "Edit quantity",
                            "Remove from cart"
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                    builder.setTitle("Cart options:");
                    builder.setItems(charSequence, (dialog, which) -> {

                        if (which == 0){
                            Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                            intent.putExtra("pid", cart.getPid());
                            startActivity(intent);
                        }
                        if (which == 1){
                            cartRef.child("Customer View").child(Prevalent.currentOnlineCustomer.getPhoneNumber())
                                    .child("Products added in Cart").child(cart.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(CartActivity.this, "Item removed successfully...", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                    builder.show();
                });

                int oneCartItemTotalPrice = (Integer.valueOf(cart.getPrice())) * Integer.valueOf(cart.getQuantity());
                totalPrice = totalPrice + oneCartItemTotalPrice;

                cartDisplayTotalPrice.setText("Ksh " + String.valueOf(totalPrice));
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                return new CartViewHolder(view);
            }
        };
        cart_recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void checkOrderState(){

        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineCustomer.getPhoneNumber());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String deliveryState = snapshot.child("state").getValue().toString();
                    String customerName = snapshot.child("firstName").getValue().toString();
                    if (deliveryState.equals("delivered")){

                        cartTotalAmountPrice.setVisibility(View.GONE);
                        cartDisplayTotalPrice.setVisibility(View.GONE);
                        cartPendingOrder.setText("Dear " + customerName + "\n your order is on route");
                        cartPendingOrder.setVisibility(View.VISIBLE);
                        cartCheckoutBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "Continue shopping after your pending order has been delivered...", Toast.LENGTH_LONG).show();
                    }
                    else if (deliveryState.equals("not delivered")){

                        cartTotalAmountPrice.setVisibility(View.GONE);
                        cartDisplayTotalPrice.setVisibility(View.GONE);
                        cartPendingOrder.setText("Dear " + customerName + "\n your order will be delivered soon...");
                        cartPendingOrder.setVisibility(View.VISIBLE);
                        cartCheckoutBtn.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Unexpected error, try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void redirectToHomeActivity(View view) {

        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void sendToCheckout(View view) {

        Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
        intent.putExtra("Total price", String.valueOf(totalPrice));
        startActivity(intent);
    }
}