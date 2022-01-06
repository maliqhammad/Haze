package com.hammad.iphoneringtones.ui.wallpapers;

import static com.hammad.iphoneringtones.MainActivity.CURRENT_POSITION;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.hammad.iphoneringtones.R;
import com.hammad.iphoneringtones.classes.DialogProgressBar;
import com.hammad.iphoneringtones.classes.DownloadBroadcastReceiver;
import com.hammad.iphoneringtones.databinding.FragmentWallpapersBinding;
import com.hammad.iphoneringtones.dialogs.WallpaperBottomSheetDialog;

import java.util.ArrayList;

public class WallpapersFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private WallpapersViewModel wallpapersViewModel;
    private FragmentWallpapersBinding binding;
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

    private void setListeners() {
        binding.recyclerViewPopularHomeFragment.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
        popularWallpaperAdapter = new PopularWallpaperAdapter(getActivity(), new PopularWallpaperAdapter.PopularWallpaperAdapterCallback() {
            @Override
            public void onDownloadWallpaper(WallpaperModel wallpaperModel) {
                WallpaperBottomSheetDialog wallpaperBottomSheetDialog = new WallpaperBottomSheetDialog(getContext(), wallpaperModel);
                wallpaperBottomSheetDialog.show(getChildFragmentManager(), "Download");
            }

            @Override
            public void onItemClick(View view, ArrayList<WallpaperModel> modelArrayList, int position) {
                CURRENT_POSITION = position;
                Intent intent = new Intent(context, DisplayFullWallpaperActivity.class);
                WallpaperListObject wallpaperListObject = new WallpaperListObject();
                wallpaperListObject.setWallpaperModelList(modelArrayList);
                intent.putExtra("list", wallpaperListObject);
                intent.putExtra("CURRENT_POSITION", CURRENT_POSITION);
                context.startActivity(intent);
            }
        });
        binding.recyclerViewPopularHomeFragment.setAdapter(popularWallpaperAdapter);
        wallpaperModelObserver = wallpaperModel -> {
            progressBar.cancelSpinnerDialog();
            popularWallpaperAdapter.updateWallpapersList(wallpaperModel);
        };
        wallpapersViewModel.getPopularData1().observe(getViewLifecycleOwner(), wallpaperModelObserver);

        featureObserver = wallpaperModels -> {
            binding.recyclerViewFeatureHomeFragment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            binding.recyclerViewFeatureHomeFragment.setAdapter(new FeaturesAdapter(getActivity(), wallpaperModels));
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