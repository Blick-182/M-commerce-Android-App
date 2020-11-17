package com.example.blacklight.HomeStatic.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.blacklight.R;

public class GalleryViewHolder extends RecyclerView.ViewHolder{

    public ImageView item_picture;
    public RelativeLayout item_picture_parent;

    public GalleryViewHolder(@NonNull View itemView) {
        super(itemView);
        item_picture = itemView.findViewById(R.id.item_picture);
        item_picture_parent = itemView.findViewById(R.id.item_picture_parent);

    }
}
