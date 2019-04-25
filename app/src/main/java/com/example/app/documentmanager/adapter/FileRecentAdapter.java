package com.example.app.documentmanager.adapter;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.app.documentmanager.R;


import java.util.List;

public class FileRecentAdapter extends RecyclerView.Adapter<FileRecentAdapter.ViewHolder> {

    private List<Bitmap> mImageList;

    public FileRecentAdapter(List<Bitmap> mImageList) {
        this.mImageList = mImageList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        //TextView recentTextView;
        ImageView recentImageView;
        public ViewHolder (View view){
            super(view);
            //recentTextView = view.findViewById(R.id.recent_text_view);
            recentImageView = view.findViewById(R.id.recent_image_view);
        }
    }

    @NonNull
    @Override
    public FileRecentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recent_file,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FileRecentAdapter.ViewHolder viewHolder, int i) {
        Bitmap bitmap = mImageList.get(i);
        viewHolder.recentImageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }
}

