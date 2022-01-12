package com.hammad.iphoneringtones.ui.wallpapers;

import static com.hammad.iphoneringtones.classes.StaticVariable.downloadWallpaper;


import androidx.viewpager2.widget.ViewPager2;

import com.hammad.iphoneringtones.R;
import com.hammad.iphoneringtones.classes.BaseActivity;
import com.hammad.iphoneringtones.classes.HorizontalMarginItemDecoration;
import com.hammad.iphoneringtones.databinding.ActivityDisplayFullWallpaperBinding;
import com.hammad.iphoneringtones.dialogs.WallpaperBottomSheetDialog;
import com.squareup.picasso.Picasso;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DisplayFullWallpaperActivity extends BaseActivity {
    private static final String TAG = "DisplayFullWallpaper";
    WallpaperListObject wallpaperListObject;
    ActivityDisplayFullWallpaperBinding binding;
    int CURRENT_POSITION;
    WallpaperManager wallpaperManager;

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
        wallpaperListObject = (WallpaperListObject) getIntent().getSerializableExtra("list");
        CURRENT_POSITION = getIntent().getIntExtra("CURRENT_POSITION", 0);
        setToolbarTitle(wallpaperListObject.getWallpaperModelList().get(CURRENT_POSITION).getWallpaperTitle());
        wallpaperManager = WallpaperManager.getInstance(this);
        Log.d(TAG, "onCreate: getWallpaperModelList: " + wallpaperListObject.getWallpaperModelList().size());
    }

    private void setListeners() {
        binding.ivDownloadActivityDisplayFullWallpaper.setOnClickListener(view -> {
            WallpaperBottomSheetDialog wallpaperBottomSheetDialog = new WallpaperBottomSheetDialog(this, wallpaperListObject.getWallpaperModelList().get(binding.viewPagerActivityDisplayFullWallpaper.getCurrentItem()), new WallpaperBottomSheetDialog.Callback() {
                @Override
                public void onDownloadWallpaper(WallpaperModel wallpaperModel) {
                    downloadWallpaper(DisplayFullWallpaperActivity.this, wallpaperModel.getWallpaperUri(), wallpaperModel.getWallpaperTitle());
                }

                @Override
                public void onSetWallpaper(WallpaperModel wallpaperModel) {
                    new Handler(Looper.getMainLooper()).post(() -> {

                        try {
                            Bitmap result = Picasso.get().load(wallpaperModel.getWallpaperUri()).get();
                            wallpaperManager.setBitmap(result);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(DisplayFullWallpaperActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
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
        binding.viewPagerActivityDisplayFullWallpaper.setAdapter(new FullWallpaperAdapter(this, wallpaperListObject.getWallpaperModelList()));
        binding.viewPagerActivityDisplayFullWallpaper.setCurrentItem(CURRENT_POSITION, true);
        binding.viewPagerActivityDisplayFullWallpaper.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d(TAG, "onPageSelected: " + position);
                setToolbarTitle(wallpaperListObject.getWallpaperModelList().get(position).getWallpaperTitle());
            }
        });
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}