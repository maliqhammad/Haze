package com.hammad.iphoneringtones.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.hammad.iphoneringtones.R;
import com.hammad.iphoneringtones.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    HomeViewModel homeViewModel;
    FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setlisteners();
        homeViewModel.getText().observe(getViewLifecycleOwner(), s -> {
        });
        return root;
    }

    private void setlisteners() {
        binding.cardViewWallpapersFragmentHome.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_wallpapers));
        binding.cardViewRingtoneFragmentHome.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_ringtones));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}