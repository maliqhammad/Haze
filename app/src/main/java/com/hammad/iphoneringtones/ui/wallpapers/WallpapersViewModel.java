package com.hammad.iphoneringtones.ui.wallpapers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hammad.iphoneringtones.classes.SingleLiveEvent;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class WallpapersViewModel extends ViewModel {
    private static final String TAG = "WallpapersViewModel";
    FirebaseStorage firebaseStorage;
    StorageReference pathReference;
    SingleLiveEvent<ArrayList<WallpaperModel>> arrayListMutableLiveData;
    SingleLiveEvent<WallpaperModel> wallpaperModelSingleLiveEvent;
    private SingleLiveEvent<ArrayList<WallpaperModel>> categoryMutableLiveData;

    public WallpapersViewModel() {

    }

    public SingleLiveEvent<ArrayList<WallpaperModel>> setCategoryList(Context context) {
        if (categoryMutableLiveData == null) {
            categoryMutableLiveData = new SingleLiveEvent<>();
            categoryMutableLiveData.setValue(getCategoryList(context));
        }
        return categoryMutableLiveData;
    }

    private ArrayList<WallpaperModel> getCategoryList(Context context) {
        ArrayList<WallpaperModel> categoryArrayList = new ArrayList<>();
        for (int i = 0; i < Categories.getFeatures(context).size(); i++) {
            WallpaperModel wallpaperModel = new WallpaperModel();
            wallpaperModel.setWallpaperTitle(Categories.getFeatures(context).get(i));
            wallpaperModel.setCategory(Categories.getCategoryNames(context).get(i));
            wallpaperModel.setImage(Categories.getFeatureImages(context).getResourceId(i, 0));
            categoryArrayList.add(wallpaperModel);
        }
        return categoryArrayList;
    }

    public SingleLiveEvent<WallpaperModel> getPopularWallpaper() {
        wallpaperModelSingleLiveEvent = new SingleLiveEvent<>();
        loadPopularWallpaper();
        return wallpaperModelSingleLiveEvent;
    }

    private void loadPopularWallpaper() {
        DatabaseReference wallpapersRef = FirebaseDatabase.getInstance().getReference().child("wallpapers");
        wallpapersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshot.child(Objects.requireNonNull(dataSnapshot.getKey())).getRef().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            wallpaperModelSingleLiveEvent.setValue(new WallpaperModel(
                                    Objects.requireNonNull(dataSnapshot.child("imageTitle").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("imageUrl").getValue()).toString()
                            ));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void retrievePopularWallpapers() {
        DatabaseReference ringtonesRef = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        pathReference = FirebaseStorage.getInstance().getReference().child("wallpapers");
        pathReference.listAll().addOnSuccessListener(listResult -> {
            ArrayList<WallpaperModel> ringtoneModelArrayList = new ArrayList<>();
            for (StorageReference item : listResult.getItems()) {
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    String key = ringtonesRef.child("wallpapers").getKey();
                    if (key != null) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("imageUrl", uri.toString());
                        hashMap.put("imageTitle", FirebaseStorage.getInstance().getReferenceFromUrl(uri.toString()).getName());
                        ringtonesRef.child(key).push().setValue(hashMap);
                    }
                    wallpaperModelSingleLiveEvent.setValue(new WallpaperModel(item.getName(), uri.toString()));
                    Log.d(TAG, "onSuccess: " + "\n | file: " + FirebaseStorage.getInstance().getReferenceFromUrl(uri.toString()).getName() + "\n | key: " + ringtonesRef.push().getKey() + "\n | uri:  " + uri);
                });
            }
        }).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.getMessage()));
    }

    public SingleLiveEvent<WallpaperModel> getWallpaperByCategory(WallpaperModel wallpaperModel) {
        wallpaperModelSingleLiveEvent = new SingleLiveEvent<>();
        loadWallpapersByCategory(wallpaperModel);
        return wallpaperModelSingleLiveEvent;
    }

    private void loadWallpapersByCategory(WallpaperModel wallpaperModel) {
        DatabaseReference wallpapersRef = FirebaseDatabase.getInstance().getReference().child("wallpapersByCategory").child(wallpaperModel.getCategory());
        wallpapersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<WallpaperModel> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshot.child(Objects.requireNonNull(dataSnapshot.getKey())).getRef().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            wallpaperModelSingleLiveEvent.setValue(new WallpaperModel(
                                    Objects.requireNonNull(dataSnapshot.child("imageTitle").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("imageUrl").getValue()).toString()
                            ));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void retrieveWallpapersByCategory(WallpaperModel wallpaperModel) {
        DatabaseReference wallpapersByCategoryRef = FirebaseDatabase.getInstance().getReference().child("wallpapersByCategory");
        firebaseStorage = FirebaseStorage.getInstance();
        pathReference = FirebaseStorage.getInstance().getReference().child("wallpapersByCategory").child(wallpaperModel.getCategory());
        pathReference.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    String key = wallpapersByCategoryRef.child(wallpaperModel.getCategory()).getKey();
                    if (key != null) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("imageUrl", uri.toString());
                        hashMap.put("imageTitle", FirebaseStorage.getInstance().getReferenceFromUrl(uri.toString()).getName());
                        wallpapersByCategoryRef.child(key).push().setValue(hashMap);
                    }
                    wallpaperModelSingleLiveEvent.setValue(new WallpaperModel(item.getName(), uri.toString()));
                    Log.d(TAG, "onSuccess: " + "\n | file: " + FirebaseStorage.getInstance().getReferenceFromUrl(uri.toString()).getName() + "\n | key: " + wallpapersByCategoryRef.push().getKey() + "\n | uri:  " + uri);
                });
            }
        }).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.getMessage()));
    }

    boolean isRetrieveCategory = false;

    public boolean isRetrieveCategory() {
        DatabaseReference configRef = FirebaseDatabase.getInstance().getReference().child("appConfiguration");
        configRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = Objects.requireNonNull(snapshot.child("isRetrieveCategory").getValue()).toString();
                isRetrieveCategory = value.equals("true");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                isRetrieveCategory = false;
            }
        });
        return isRetrieveCategory;
    }
}