package com.hammad.iphoneringtones.ui.wallpapers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.hammad.iphoneringtones.R;
import com.hammad.iphoneringtones.classes.ColorGenerator;
import com.hammad.iphoneringtones.classes.GlideImageLoader;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.HomeAdapterViewHolder> {
    Context context;
    ArrayList<WallpaperModel> arrayList;
    FeaturesAdapterCallback callback;

    public CategoriesAdapter(Context context, FeaturesAdapterCallback callback) {
        this.context = context;
        this.callback = callback;
        arrayList = new ArrayList<>();
    }

    @NonNull
    @NotNull
    @Override
    public HomeAdapterViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new HomeAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.categories_adapter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HomeAdapterViewHolder holder, int position) {
        holder.view_categories_adapter_item.setBackgroundColor(holder.colorGenerator.getRandomColor());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.home_bg)
                .error(R.drawable.home_bg)
                .priority(Priority.HIGH);

        new GlideImageLoader(holder.iv_categories_adapter_item, holder.progress_bar_categories_adapter_item).load(arrayList.get(position).getWallpaperUri(), options);

        holder.tv_categories_adapter_item.setText(capitalize(arrayList.get(position).getCategory()));
        holder.itemView.setOnClickListener(view -> callback.onItemClick(arrayList.get(position)));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class HomeAdapterViewHolder extends RecyclerView.ViewHolder {
        View view_categories_adapter_item;
        CardView card_view_categories_adapter_item;
        ImageView iv_categories_adapter_item;
        ProgressBar progress_bar_categories_adapter_item;
        TextView tv_categories_adapter_item;
        ColorGenerator colorGenerator = ColorGenerator.MATERIAL;

        public HomeAdapterViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            card_view_categories_adapter_item = itemView.findViewById(R.id.card_view_categories_adapter_item);
            view_categories_adapter_item = itemView.findViewById(R.id.view_categories_adapter_item);
            iv_categories_adapter_item = itemView.findViewById(R.id.iv_categories_adapter_item);
            progress_bar_categories_adapter_item = itemView.findViewById(R.id.progress_bar_categories_adapter_item);
            tv_categories_adapter_item = itemView.findViewById(R.id.tv_categories_adapter_item);
        }
    }

    public void updateCategoryList(WallpaperModel wallpaperModel) {
        arrayList.add(wallpaperModel);
        notifyDataSetChanged();
    }

    public String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, Objects.requireNonNull(capMatcher.group(1)).toUpperCase() + Objects.requireNonNull(capMatcher.group(2)).toLowerCase());
        }
        return capMatcher.appendTail(capBuffer).toString();
    }

    public interface FeaturesAdapterCallback {
        void onItemClick(WallpaperModel wallpaperModel);
    }
}
