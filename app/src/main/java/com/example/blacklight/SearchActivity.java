package com.example.blacklight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.blacklight.Models.Products;
import com.example.blacklight.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {

    private EditText searchProduct;
    private Button searchProductBtn;

    private RecyclerView searchProduct_recyclerView;

    private String searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchProduct = findViewById(R.id.search_product);
        searchProductBtn = findViewById(R.id.search_product_button);

        searchProduct_recyclerView = findViewById(R.id.search_product_recyclerview);
        searchProduct_recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
    }

    public void searchForProduct(View view) {

        searchInput = searchProduct.getText().toString();
        onStart();
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(reference.orderByChild("name").startAt(searchInput), Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {

                Picasso.get().load(products.getImage()).into(productViewHolder.displayProductImage);
                productViewHolder.displayProductName.setText(products.getName());
                //  productViewHolder.displayProductDescription.setText(products.getDescription());
                productViewHolder.displayProductPrice.setText("Ksh " + products.getPrice());

                productViewHolder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(SearchActivity.this, ProductDetailsActivity.class);
                    intent.putExtra("pid", products.getPid());
                    startActivity(intent);
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                return new ProductViewHolder(view);
            }
        };
        searchProduct_recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}