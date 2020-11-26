package com.example.blacklight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MerchantChooseInventoryCategoryActivity extends AppCompatActivity {

    private Toolbar chooseInventoryCategoryToolBar;
    private ImageView phoneImageThumbnail, laptopImageThumbnail, TVsImageThumbnail, AccessoriesImageThumbnail, KitchenImageThumbnail, foodImageThumbnail, stationaryImageThumbnail, toiletriesImageThumbnail, eBooksImageThumbnail, moviesImageThumbnail, alcoholImageThumbnail;
    private TextView phoneCategoryTitle, laptopCategoryTitle, TVsCategoryTitle, AccessoriesCategoryTitle, KitchenCategoryTitle, foodCategoryTitle, stationaryCategoryTitle, toiletriesCategoryTitle, eBooksCategoryTitle, moviesCategoryTitle, alcoholCategoryTitle;
    private Button addPhone_btn, addLaptop_btn, addTVs_btn, addAccessories_btn, addKitchenAppliances_btn, addFood_btn, addStationary_btn, addToiletries_btn, addE_Books_btn, addMovies_btn, addAlcohol_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_choose_inventory_category);

        chooseInventoryCategoryToolBar = findViewById(R.id.merchant_choose_category_toolbar);

        foodImageThumbnail = findViewById(R.id.add_food_image);
        foodCategoryTitle = findViewById(R.id.add_food_title);
        addFood_btn = findViewById(R.id.add_food_button);

        stationaryImageThumbnail = findViewById(R.id.add_stationary_image);
        stationaryCategoryTitle = findViewById(R.id.add_stationary_title);
        addStationary_btn = findViewById(R.id.add_stationary_button);

        toiletriesImageThumbnail = findViewById(R.id.add_toiletries_image);
        toiletriesCategoryTitle = findViewById(R.id.add_toiletries_title);
        addToiletries_btn = findViewById(R.id.add_toiletries_button);

        eBooksImageThumbnail = findViewById(R.id.add_e_books_image);
        eBooksCategoryTitle = findViewById(R.id.add_e_books_title);
        addE_Books_btn = findViewById(R.id.add_e_books_button);

        moviesImageThumbnail = findViewById(R.id.add_movies_TV_shows_image);
        moviesCategoryTitle = findViewById(R.id.add_movies_TV_shows_title);
        addMovies_btn = findViewById(R.id.add_movies_TV_shows_button);

        alcoholImageThumbnail = findViewById(R.id.add_alcohol_image);
        alcoholCategoryTitle = findViewById(R.id.add_alcohol_title);
        addAlcohol_btn = findViewById(R.id.add_alcohol_button);

        phoneImageThumbnail = findViewById(R.id.add_phone_image);
        phoneCategoryTitle = findViewById(R.id.add_phone_title);
        addPhone_btn = findViewById(R.id.add_phone_button);

        laptopImageThumbnail = findViewById(R.id.add_laptop_image);
        laptopCategoryTitle = findViewById(R.id.add_laptop_title);
        addLaptop_btn = findViewById(R.id.add_laptop_button);

        TVsImageThumbnail = findViewById(R.id.add_TVs_image);
        TVsCategoryTitle = findViewById(R.id.add_TVs_title);
        addTVs_btn = findViewById(R.id.add_TV_button);

        AccessoriesImageThumbnail = findViewById(R.id.add_accessories_image);
        AccessoriesCategoryTitle = findViewById(R.id.add_accessories_title);
        addAccessories_btn = findViewById(R.id.add_accessories_button);

        KitchenImageThumbnail = findViewById(R.id.add_kitchen_appliances_image);
        KitchenCategoryTitle = findViewById(R.id.add_kitchen_appliances_title);
        addKitchenAppliances_btn = findViewById(R.id.add_kitchen_appliances_button);

        addFood_btn.setOnClickListener(v -> {
            Intent intent = new Intent(MerchantChooseInventoryCategoryActivity.this, MerchantAddNewProductActivity.class);
            intent.putExtra("category","Food and Drinks");
            startActivity(intent);
        });

        addStationary_btn.setOnClickListener(v -> {
            Intent intent = new Intent(MerchantChooseInventoryCategoryActivity.this, MerchantAddNewProductActivity.class);
            intent.putExtra("category","Stationary");
            startActivity(intent);
        });

        addToiletries_btn.setOnClickListener(v -> {
            Intent intent = new Intent(MerchantChooseInventoryCategoryActivity.this, MerchantAddNewProductActivity.class);
            intent.putExtra("category","Toiletries");
            startActivity(intent);
        });

        addE_Books_btn.setOnClickListener(v -> {
            Intent intent = new Intent(MerchantChooseInventoryCategoryActivity.this, MerchantAddNewProductActivity.class);
            intent.putExtra("category","E-books");
            startActivity(intent);
        });

        addMovies_btn.setOnClickListener(v -> {
            Intent intent = new Intent(MerchantChooseInventoryCategoryActivity.this, MerchantAddNewProductActivity.class);
            intent.putExtra("category","Movies and TV Series");
            startActivity(intent);
        });

        addAlcohol_btn.setOnClickListener(v -> {
            Intent intent = new Intent(MerchantChooseInventoryCategoryActivity.this, MerchantAddNewProductActivity.class);
            intent.putExtra("category","Alcohol and Cocktails");
            startActivity(intent);
        });

        addPhone_btn.setOnClickListener(v -> {
            Intent intent = new Intent(MerchantChooseInventoryCategoryActivity.this, MerchantAddNewProductActivity.class);
            intent.putExtra("category","Phones and Tablets");
            startActivity(intent);
        });

        addLaptop_btn.setOnClickListener(v -> {
            Intent intent = new Intent(MerchantChooseInventoryCategoryActivity.this, MerchantAddNewProductActivity.class);
            intent.putExtra("category","Laptops and PCs");
            startActivity(intent);
        });

        addTVs_btn.setOnClickListener(v -> {
            Intent intent = new Intent(MerchantChooseInventoryCategoryActivity.this, MerchantAddNewProductActivity.class);
            intent.putExtra("category","TVs and Monitors");
            startActivity(intent);
        });

        addAccessories_btn.setOnClickListener(v -> {
            Intent intent = new Intent(MerchantChooseInventoryCategoryActivity.this, MerchantAddNewProductActivity.class);
            intent.putExtra("category","Accessories");
            startActivity(intent);
        });

        addKitchenAppliances_btn.setOnClickListener(v -> {
            Intent intent = new Intent(MerchantChooseInventoryCategoryActivity.this, MerchantAddNewProductActivity.class);
            intent.putExtra("category","Kitchen Appliances");
            startActivity(intent);
        });
    }

    public void sendToMerchantStore(View view) {

        Intent intent = new Intent(MerchantChooseInventoryCategoryActivity.this, MerchantStoreActivity.class);
        startActivity(intent);
        finish();
    }
}