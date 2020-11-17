package com.example.blacklight.HomeStatic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blacklight.HomeStatic.model.Shop;
import com.example.blacklight.HomeStatic.viewholder.GalleryViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.example.blacklight.R;

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Shop shop;
    private int imageSize;

    public GalleryAdapter(Context context, Shop shop, int imageSize) {
        this.context = context;
        this.shop = shop;
        this.imageSize = imageSize;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        setImageSize(((GalleryViewHolder) holder).item_picture);
        setPicture(((GalleryViewHolder) holder).item_picture, shop.getPictures().get(position).getPhotoURL());
    }

    @Override
    public int getItemCount() {
        return shop.getPictures().size();
    }

    private void setImageSize(ImageView imageView) {
        imageView.getLayoutParams().height = imageSize;
        imageView.getLayoutParams().width = imageSize;
    }

    private void setPicture(final ImageView imageView, String pictureURL) {
        Picasso.get().load(pictureURL).fit().centerCrop().into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                imageView.setBackgroundResource(R.drawable.project_blacklight);
            }
        });
    }
}
