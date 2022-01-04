package com.hammad.iphoneringtones.ui.wallpapers;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.hammad.iphoneringtones.R;
import com.tbuonomo.creativeviewpager.CreativeViewPager;
import com.tbuonomo.creativeviewpager.adapter.CreativeContentAdapter;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;

public class DisplayFullWallpaperActivity extends AppCompatActivity {
    ArrayList<WallpaperModel> wallpaperModelArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_full_wallpaper);
        wallpaperModelArrayList = (ArrayList<WallpaperModel>) getIntent().getExtras().getSerializable("list");
        Log.d("TAG", "onCreate: "+wallpaperModelArrayList.size());
        CreativeViewPager creativeViewPagerView = findViewById(R.id.creativeViewPagerView);
        creativeViewPagerView.setCreativeViewPagerAdapter(new Adapter(this, wallpaperModelArrayList));
    }
}