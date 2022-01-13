package com.hammad.iphoneringtones;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.hammad.iphoneringtones.classes.BaseActivity;
import com.hammad.iphoneringtones.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {
    public static int CURRENT_POSITION = 0;
    private AppBarConfiguration mAppBarConfiguration;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        setNavigationController();
    }

    private void setNavigationController() {
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_wallpapers, R.id.nav_ringtones).setOpenableLayout(binding.drawerLayout).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupWithNavController(binding.appBarMain.bottomNavigationMainActivity, navController);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        binding.drawerLayout.open();
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
}