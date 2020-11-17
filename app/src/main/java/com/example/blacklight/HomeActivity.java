package com.example.blacklight;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blacklight.Models.Products;
import com.example.blacklight.Prevalent.Prevalent;
import com.example.blacklight.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    private TextView side_nav_username;
    private RecyclerView home_recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private DatabaseReference ProductsRef;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        home_recyclerView = findViewById(R.id.home_recyclerview);

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        Paper.init(this);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("Blacklight");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.home_fab_button);

     /*   fab.setOnClickListener(view -> Snackbar.make(view, "Proceeding to cart", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()); */

        fab.setOnClickListener(v -> {

            //Snackbar.make(v, "Proceeding", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

       /* ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, "Open navigation drawer", "Close navigation drawer");
        drawer.addDrawerListener(toggle);
        toggle.syncState(); */

        NavigationView navigationView = findViewById(R.id.home_nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

                mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

       /* View headerView = navigationView.getHeaderView(0);
        TextView side_nav_username = headerView.findViewById(R.id.side_nav_customer_profile_name);
        CircleImageView sideNavProfileImage = headerView.findViewById(R.id.side_nav_customer_profile_name);

        side_nav_username.setText(Prevalent.currentOnlineCustomer.getUsername());  */

        home_recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        home_recyclerView.setLayoutManager(layoutManager);

        onStart();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(ProductsRef, Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {

                Picasso.get().load(products.getImage()).into(productViewHolder.displayProductImage);
                productViewHolder.displayProductName.setText(products.getName());
              //  productViewHolder.displayProductDescription.setText(products.getDescription());
                productViewHolder.displayProductPrice.setText("Ksh " + products.getPrice());

                productViewHolder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
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
        home_recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void sendToHomeActivity(MenuItem item) {
        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void sendToProfileActivity(MenuItem item) {
        Intent intent = new Intent(HomeActivity.this, EditProfileActivity.class);
        startActivity(intent);
       // finish();
    }

    public void sendToSearchActivity(MenuItem item) {
        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    public void sendToShopByCategoryActivity(MenuItem item) {

    }

    public void sendToCartActivity(MenuItem item) {
        Intent intent = new Intent(HomeActivity.this, CartActivity.class);
        startActivity(intent);
    }

    public void sendToOrdersActivity(MenuItem item) {

    }

    public void sendToSettingsActivity(MenuItem item) {

    }

    public void sendToFeedbackActivity(MenuItem item) {
        Intent intent = new Intent(HomeActivity.this, FeedbackActivity.class);
        startActivity(intent);
    }

    public void signOutCustomerActivity(MenuItem item) {

        Paper.book().destroy();

        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}