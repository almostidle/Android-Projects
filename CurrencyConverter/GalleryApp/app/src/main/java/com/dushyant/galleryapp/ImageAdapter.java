package com.dushyant.galleryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    interface OnClick {
        void onClick(File f);
    }

    List<File> list;
    OnClick listener;

    public ImageAdapter(List<File> list, OnClick listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        File f = list.get(pos);

        // load image with glide
        Glide.with(holder.iv.getContext()).load(f).centerCrop().into(holder.iv);

        holder.iv.setOnClickListener(v -> listener.onClick(f));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        ViewHolder(View v) {
            super(v);
            iv = v.findViewById(R.id.imgThumb);
        }
    }
}