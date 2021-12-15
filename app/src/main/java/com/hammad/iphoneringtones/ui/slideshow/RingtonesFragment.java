package com.hammad.iphoneringtones.ui.slideshow;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.iphoneringtones.databinding.FragmentSlideshowBinding;

import java.util.ArrayList;

public class RingtonesFragment extends Fragment {

    private RingtonesViewModel ringtonesViewModel;
    private FragmentSlideshowBinding binding;
    RecyclerView recyclerViewRingtones;
    SongAdapter songAdapter;
    Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ringtonesViewModel = new ViewModelProvider(this).get(RingtonesViewModel.class);
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setIds();
        setRecyclerView();
        ringtonesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    private void setIds() {
        recyclerViewRingtones = binding.recyclerViewRingtones;

    }

    private void setRecyclerView() {
        recyclerViewRingtones.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        songAdapter = new SongAdapter(context,SongProvider.INSTANCE.getAllSongs(context), new OnSongItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(context, "onItemClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSetRingClick(int position) {
                Toast.makeText(context, "onSetRingClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPlayerRingClick(int position) {
                Toast.makeText(context, "onPlayerRingClick", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerViewRingtones.setAdapter(songAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}