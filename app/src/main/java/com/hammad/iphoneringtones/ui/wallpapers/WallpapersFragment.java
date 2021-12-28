package com.hammad.iphoneringtones.ui.wallpapers;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.hammad.iphoneringtones.databinding.FragmentWallpapersBinding;
import com.hammad.iphoneringtones.dialogs.DialogBottomSheet;

import java.util.ArrayList;

public class WallpapersFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private WallpapersViewModel wallpapersViewModel;
    private FragmentWallpapersBinding binding;
    RecyclerView recycler_view_popular_home_fragment, recyclerViewFeatureHomeFragment;
    ArrayList<WallpaperModel> wallpaperModelArrayList;
    Observer<ArrayList<WallpaperModel>> imagesObserver, categoriesObserver;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        wallpapersViewModel = new ViewModelProvider(this).get(WallpapersViewModel.class);
        binding = FragmentWallpapersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initialize();
        bindViews(binding);
        setListeners();
//        wallpapersViewModel.retrieveWallpapers();
        return root;
    }

    private void initialize() {
        wallpaperModelArrayList = new ArrayList<>();
    }

    private void bindViews(FragmentWallpapersBinding binding) {
        recycler_view_popular_home_fragment = binding.recyclerViewPopularHomeFragment;
        recyclerViewFeatureHomeFragment = binding.recyclerViewFeatureHomeFragment;
    }

    private void setListeners() {
        imagesObserver = wallpaperModels -> {
            recycler_view_popular_home_fragment.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
            recycler_view_popular_home_fragment.setAdapter(new PopularWallpaperAdapter(getActivity(), wallpaperModels, wallpaperModel -> {
                DialogBottomSheet dialogBottomSheet = new DialogBottomSheet(getContext(), wallpaperModel, null);
                dialogBottomSheet.show(getChildFragmentManager(), "Download");
            }));
        };
        wallpapersViewModel.getPopularData().observe(getViewLifecycleOwner(), imagesObserver);

        categoriesObserver = wallpaperModels -> {
            recyclerViewFeatureHomeFragment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewFeatureHomeFragment.setAdapter(new FeaturesAdapter(getActivity(), wallpaperModels));
        };
        wallpapersViewModel.setFeaturesData(getContext()).observe(getViewLifecycleOwner(), categoriesObserver);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        wallpapersViewModel.getPopularData().removeObserver(imagesObserver);
        wallpapersViewModel.setFeaturesData(getContext()).removeObserver(categoriesObserver);
        Log.d(TAG, "onDestroyView: ");
    }
}