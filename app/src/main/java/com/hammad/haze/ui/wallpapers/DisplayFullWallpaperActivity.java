package com.hammad.haze.ui.wallpapers;


import androidx.viewpager2.widget.ViewPager2;

import com.hammad.haze.classes.HorizontalMarginItemDecoration;
import com.hammad.haze.classes.WallpaperHelperUtils;
import com.hammad.haze.dialogs.WallpaperBottomSheetDialog;
import com.hammad.haze.R;
import com.hammad.haze.classes.BaseActivity;
import com.hammad.haze.databinding.ActivityDisplayFullWallpaperBinding;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class DisplayFullWallpaperActivity extends BaseActivity {
    private static final String TAG = "DisplayFullWallpaper";
    ArrayList<WallpaperModel> wallpaperModelArrayList;
    ActivityDisplayFullWallpaperBinding binding;
    int CURRENT_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDisplayFullWallpaperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getIntentData();
        setListeners();
        setupCarousel();
    }

    private void getIntentData() {
        wallpaperModelArrayList = (ArrayList<WallpaperModel>) getIntent().getSerializableExtra("list");
        CURRENT_POSITION = getIntent().getIntExtra("CURRENT_POSITION", 0);
        setToolbarTitle(capitalize(wallpaperModelArrayList.get(CURRENT_POSITION).getWallpaperTitle()));
        Log.d(TAG, "onCreate: getWallpaperModelList: " + wallpaperModelArrayList.size());
    }

    private void setListeners() {
        binding.ivDownloadActivityDisplayFullWallpaper.setOnClickListener(view -> {
            WallpaperBottomSheetDialog wallpaperBottomSheetDialog = new WallpaperBottomSheetDialog(this, wallpaperModelArrayList.get(binding.viewPagerActivityDisplayFullWallpaper.getCurrentItem()), new WallpaperBottomSheetDialog.Callback() {
                @Override
                public void onDownloadWallpaper(WallpaperModel wallpaperModel) {
                    WallpaperHelperUtils.downloadWallpaper(DisplayFullWallpaperActivity.this, wallpaperModel.getWallpaperUri(), wallpaperModel.getWallpaperTitle());
                }

                @Override
                public void onSetAsWallpaper(WallpaperModel wallpaperModel) {
                    setAsWallpaper(DisplayFullWallpaperActivity.this, wallpaperModel, false, false);
                }

                @Override
                public void onSetAsLockScreen(WallpaperModel wallpaperModel) {
                    setAsWallpaper(DisplayFullWallpaperActivity.this, wallpaperModel, true, false);
                }

                @Override
                public void onSetAsBoth(WallpaperModel wallpaperModel) {
                    setAsWallpaper(DisplayFullWallpaperActivity.this, wallpaperModel, true, true);
                }
            });
            wallpaperBottomSheetDialog.show(getSupportFragmentManager(), "Download");
        });
    }

    private void setupCarousel() {
        binding.viewPagerActivityDisplayFullWallpaper.setOffscreenPageLimit(1);
        float nextItemVisiblePx = getResources().getDimension(R.dimen._15sdp);
        float currentItemHorizontalMarginPx = getResources().getDimension(R.dimen._20sdp);
        final float pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx;
        ViewPager2.PageTransformer pageTransformer = (page, position) -> {
            page.setTranslationX(-pageTranslationX * position);
            page.setScaleY((float) 1 - 0.25F * Math.abs(position));
            page.setAlpha(0.25F + ((float) 1 - Math.abs(position)));
        };
        binding.viewPagerActivityDisplayFullWallpaper.setPageTransformer(pageTransformer);
        HorizontalMarginItemDecoration itemDecoration = new HorizontalMarginItemDecoration(this, R.dimen._20sdp);
        binding.viewPagerActivityDisplayFullWallpaper.addItemDecoration(itemDecoration);
        binding.viewPagerActivityDisplayFullWallpaper.setAdapter(new FullWallpaperAdapter(this, wallpaperModelArrayList));
        binding.viewPagerActivityDisplayFullWallpaper.setCurrentItem(CURRENT_POSITION, true);
        binding.viewPagerActivityDisplayFullWallpaper.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d(TAG, "onPageSelected: " + position);
                setToolbarTitle(capitalize(wallpaperModelArrayList.get(position).getWallpaperTitle()));
            }
        });
    }
}