package com.hammad.haze.ui.wallpapers;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.hammad.haze.MainActivity;
import com.hammad.haze.classes.BaseFragment;
import com.hammad.haze.classes.DownloadBroadcastReceiver;
import com.hammad.haze.R;
import com.hammad.haze.databinding.FragmentWallpapersBinding;

public class WallpapersFragment extends BaseFragment {
    private static final String TAG = "WallpapersFragment";
    WallpapersViewModel wallpapersViewModel;
    FragmentWallpapersBinding binding;
    PopularWallpaperAdapter popularWallpaperAdapter;
    CategoriesAdapter categoriesAdapter;
    Observer<WallpaperModel> categoryObserver;
    Observer<WallpaperModel> wallpaperModelObserver;
    Observer<WallpaperModel> wallpaperByCategoryObserver;
    Context context;
    DownloadBroadcastReceiver receiver;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        wallpapersViewModel = new ViewModelProvider(this).get(WallpapersViewModel.class);
        binding = FragmentWallpapersBinding.inflate(inflater, container, false);
        Log.d(TAG, "onCreateView: ");
        View root = binding.getRoot();
        initialize();
        setListeners();
        setPopularWallpaperRecyclerView();
        setCategoryRecyclerView();
//        wallpapersViewModel.retrievePopularWallpapers(context);
//        wallpapersViewModel.retrieveCategories(context);
        return root;
    }

    private void initialize() {
        receiver = new DownloadBroadcastReceiver(context.getResources().getString(R.string.wallpaper));
        context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        binding.tvPopularLabelHomeFragment.setVisibility(View.INVISIBLE);
        binding.tvCategoryLabelHomeFragment.setVisibility(View.INVISIBLE);
    }

    private void setListeners() {
        binding.tvButtonClearCategoryHomeFragment.setOnClickListener(view -> {
            setPopularWallpaperRecyclerView();
            binding.tvPopularLabelHomeFragment.setVisibility(View.VISIBLE);
            binding.tvButtonClearCategoryHomeFragment.setVisibility(View.INVISIBLE);
        });
    }

    private void setCategoryRecyclerView() {
        binding.recyclerViewCategoryHomeFragment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoriesAdapter = new CategoriesAdapter(context, wallpaperModel -> wallpapersViewModel.isRetrieveCategory(isRetrieveCategory -> {
            if (isRetrieveCategory) {
                wallpapersViewModel.retrieveWallpapersByCategory(context, wallpaperModel);
            } else {
                setWallpaperByCategory(wallpaperModel);
            }
        }));
        binding.recyclerViewCategoryHomeFragment.setAdapter(categoriesAdapter);
        categoryObserver = wallpaperModels -> {
            categoriesAdapter.updateCategoryList(wallpaperModels);
            binding.tvCategoryLabelHomeFragment.setVisibility(View.VISIBLE);
        };
        wallpapersViewModel.getCategoryList(getContext()).observe(getViewLifecycleOwner(), categoryObserver);
    }

    private void setPopularWallpaperRecyclerView() {
        binding.recyclerViewPopularHomeFragment.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
        popularWallpaperAdapter = new PopularWallpaperAdapter(getActivity(), (view, modelArrayList, position) -> {
            MainActivity.CURRENT_POSITION = position;
            Intent intent = new Intent(context, DisplayFullWallpaperActivity.class);
            intent.putExtra("list", modelArrayList);
            intent.putExtra("CURRENT_POSITION", MainActivity.CURRENT_POSITION);
            context.startActivity(intent);
        });
        binding.recyclerViewPopularHomeFragment.setAdapter(popularWallpaperAdapter);
        wallpaperModelObserver = wallpaperModel -> {
            popularWallpaperAdapter.updateWallpapersList(wallpaperModel);
            binding.tvPopularLabelHomeFragment.setVisibility(View.VISIBLE);
        };
        wallpapersViewModel.getPopularWallpaper(context).observe(getViewLifecycleOwner(), wallpaperModelObserver);
    }

    private void setWallpaperByCategory(WallpaperModel wallpaperModel) {
        binding.tvPopularLabelHomeFragment.setVisibility(View.INVISIBLE);
        binding.tvButtonClearCategoryHomeFragment.setVisibility(View.VISIBLE);
        binding.tvButtonClearCategoryHomeFragment.setText(capitalize(wallpaperModel.getCategory()));

        binding.recyclerViewPopularHomeFragment.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
        popularWallpaperAdapter = new PopularWallpaperAdapter(getActivity(), (view, modelArrayList, position) -> {
            MainActivity.CURRENT_POSITION = position;
            Intent intent = new Intent(context, DisplayFullWallpaperActivity.class);
            intent.putExtra("list", modelArrayList);
            intent.putExtra("CURRENT_POSITION", MainActivity.CURRENT_POSITION);
            context.startActivity(intent);
        });
        binding.recyclerViewPopularHomeFragment.setAdapter(popularWallpaperAdapter);
        wallpaperByCategoryObserver = wallpaperModels -> popularWallpaperAdapter.updateWallpapersList(wallpaperModels);
        wallpapersViewModel.getWallpaperByCategory(context, wallpaperModel).observe(getViewLifecycleOwner(), wallpaperByCategoryObserver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        wallpapersViewModel.getPopularWallpaper(context).removeObserver(wallpaperModelObserver);
        wallpapersViewModel.getCategoryList(getContext()).removeObserver(categoryObserver);
        context.unregisterReceiver(receiver);
    }

}