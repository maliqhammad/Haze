package com.hammad.iphoneringtones.ui.home;

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


import com.hammad.iphoneringtones.databinding.FragmentHomeBinding;
import com.hammad.iphoneringtones.dialogs.DialogBottomSheet;
import com.hammad.iphoneringtones.ui.wallpapers.WallpaperModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    RecyclerView recycler_view_popular_home_fragment, recyclerViewFeatureHomeFragment;
    ArrayList<WallpaperModel> wallpaperModelArrayList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initialize();
        bindViews(binding);
        setListeners();
//        retrieveImages();
        return root;
    }

    private void initialize() {
        wallpaperModelArrayList = new ArrayList<>();
    }
//
//    private void retrieveImages() {
//        DatabaseReference wallpapersRef = FirebaseDatabase.getInstance().getReference().child("wallpapers");
//        wallpapersRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    dataSnapshot.child(Objects.requireNonNull(dataSnapshot.getKey())).getRef().addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            WallpaperModel wallpaperModel = new WallpaperModel();
//                            wallpaperModel.setUri(Uri.parse(Objects.requireNonNull(dataSnapshot.child("imageUrl").getValue()).toString()));
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });
//    }

    private void bindViews(FragmentHomeBinding binding) {
        recycler_view_popular_home_fragment = binding.recyclerViewPopularHomeFragment;
        recyclerViewFeatureHomeFragment = binding.recyclerViewFeatureHomeFragment;
    }

    Observer<ArrayList<WallpaperModel>> imagesObserver, categoriesObserver;

    private void setListeners() {
        imagesObserver = wallpaperModels -> {
            recycler_view_popular_home_fragment.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
            recycler_view_popular_home_fragment.setAdapter(new PopularWallpaperAdapter(getActivity(), wallpaperModels, wallpaperModel -> {
                DialogBottomSheet dialogBottomSheet = new DialogBottomSheet(getContext(), wallpaperModel, null);
                dialogBottomSheet.show(getChildFragmentManager(), "Download");
            }));
        };
        homeViewModel.getPopularData().observe(getViewLifecycleOwner(), imagesObserver);

        categoriesObserver = wallpaperModels -> {
            recyclerViewFeatureHomeFragment.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerViewFeatureHomeFragment.setAdapter(new FeaturesAdapter(getActivity(), wallpaperModels));
        };
        homeViewModel.setFeaturesData(getContext()).observe(getViewLifecycleOwner(), categoriesObserver);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        homeViewModel.getPopularData().removeObserver(imagesObserver);
        homeViewModel.setFeaturesData(getContext()).removeObserver(categoriesObserver);
        Log.d(TAG, "onDestroyView: ");
    }
}