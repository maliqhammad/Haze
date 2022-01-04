package com.hammad.iphoneringtones.ui.wallpapers;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.hammad.iphoneringtones.R;
import com.hammad.iphoneringtones.classes.DialogProgressBar;
import com.hammad.iphoneringtones.classes.DownloadBroadcastReceiver;
import com.hammad.iphoneringtones.customViews.SpannedGridLayoutManager;
import com.hammad.iphoneringtones.databinding.FragmentWallpapersBinding;
import com.hammad.iphoneringtones.dialogs.WallpaperBottomSheetDialog;

import java.util.ArrayList;

public class WallpapersFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private WallpapersViewModel wallpapersViewModel;
    private FragmentWallpapersBinding binding;
    RecyclerView recycler_view_popular_home_fragment, recyclerViewFeatureHomeFragment;
    PopularWallpaperAdapter popularWallpaperAdapter;
    Observer<ArrayList<WallpaperModel>> featureObserver;
    Observer<WallpaperModel> wallpaperModelObserver;
    DialogProgressBar progressBar;
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
        bindViews(binding);
        setListeners();
//        wallpapersViewModel.retrieveWallpapers();
        return root;
    }

    private void initialize() {
        receiver = new DownloadBroadcastReceiver(context.getResources().getString(R.string.wallpaper_download_success));
        context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        progressBar = new DialogProgressBar(context);
        progressBar.showSpinnerDialog();
    }

    private void bindViews(FragmentWallpapersBinding binding) {
        recycler_view_popular_home_fragment = binding.recyclerViewPopularHomeFragment;
        recyclerViewFeatureHomeFragment = binding.recyclerViewFeatureHomeFragment;
    }

    private void setListeners() {
//        SpannedGridLayoutManager manager = new SpannedGridLayoutManager(
//                position -> {
//                    // Conditions for 2x2 items
//                    if (position % 6 == 0 || position % 6 == 4) {
//                        return new SpannedGridLayoutManager.SpanInfo(2, 2);
//                    } else {
//                        return new SpannedGridLayoutManager.SpanInfo(1, 1);
//                    }
//                },
//                3, // number of columns
//                1f // how big is default item
//        );
//        recycler_view_popular_home_fragment.setLayoutManager(manager);
        recycler_view_popular_home_fragment.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
        popularWallpaperAdapter = new PopularWallpaperAdapter(getActivity(), new PopularWallpaperAdapter.PopularWallpaperAdapterCallback() {
            @Override
            public void onDownloadWallpaper(WallpaperModel wallpaperModel) {
                WallpaperBottomSheetDialog wallpaperBottomSheetDialog = new WallpaperBottomSheetDialog(getContext(), wallpaperModel);
                wallpaperBottomSheetDialog.show(getChildFragmentManager(), "Download");
            }

            @Override
            public void onItemClick(View view, ArrayList<WallpaperModel> modelArrayList) {
//                Navigation.findNavController(view).navigate(R.id.nav_full_screen_wallpaper);
//                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.nav_full_screen_wallpaper);
                Intent intent = new Intent(context, DisplayFullWallpaperActivity.class);
                intent.putExtra("list", modelArrayList);
                startActivity(intent);
            }
        });
        recycler_view_popular_home_fragment.setAdapter(popularWallpaperAdapter);
        wallpaperModelObserver = wallpaperModel -> {
            progressBar.cancelSpinnerDialog();
            popularWallpaperAdapter.updateWallpapersList(wallpaperModel);
        };
        wallpapersViewModel.getPopularData1().observe(getViewLifecycleOwner(), wallpaperModelObserver);

        featureObserver = wallpaperModels -> {
            recyclerViewFeatureHomeFragment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewFeatureHomeFragment.setAdapter(new FeaturesAdapter(getActivity(), wallpaperModels));
        };
        wallpapersViewModel.setFeaturesData(getContext()).observe(getViewLifecycleOwner(), featureObserver);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        wallpapersViewModel.getPopularData1().removeObserver(wallpaperModelObserver);
        wallpapersViewModel.setFeaturesData(getContext()).removeObserver(featureObserver);
        context.unregisterReceiver(receiver);
    }
}