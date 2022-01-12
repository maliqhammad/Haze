package com.hammad.iphoneringtones.ui.wallpapers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.hammad.iphoneringtones.R;
import com.hammad.iphoneringtones.classes.ColorGenerator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.HomeAdapterViewHolder> {
    Context context;
    ArrayList<WallpaperModel> arrayList;
    FeaturesAdapterCallback callback;

    public CategoriesAdapter(Context context, ArrayList<WallpaperModel> arrayList, FeaturesAdapterCallback callback) {
        this.context = context;
        this.arrayList = arrayList;
        this.callback = callback;
    }

    @NonNull
    @NotNull
    @Override
    public HomeAdapterViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new HomeAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.features_adapter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HomeAdapterViewHolder holder, int position) {
        holder.view_features_adapter_item.setBackgroundColor(holder.colorGenerator.getRandomColor());
        holder.tv_features_adapter_item.setText(arrayList.get(position).getCategory());
        holder.iv_features_adapter_item.setImageResource(arrayList.get(position).getImage());
        holder.itemView.setOnClickListener(view -> callback.onItemClick(arrayList.get(position)));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class HomeAdapterViewHolder extends RecyclerView.ViewHolder {
        View view_features_adapter_item;
        CardView card_view_features_adapter_item;
        ImageView iv_features_adapter_item;
        TextView tv_features_adapter_item;
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;

        public HomeAdapterViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            card_view_features_adapter_item = itemView.findViewById(R.id.card_view_features_adapter_item);
            view_features_adapter_item = itemView.findViewById(R.id.view_features_adapter_item);
            iv_features_adapter_item = itemView.findViewById(R.id.iv_features_adapter_item);
            tv_features_adapter_item = itemView.findViewById(R.id.tv_features_adapter_item);
        }
    }

    public interface FeaturesAdapterCallback {
        void onItemClick(WallpaperModel wallpaperModel);
    }
}
