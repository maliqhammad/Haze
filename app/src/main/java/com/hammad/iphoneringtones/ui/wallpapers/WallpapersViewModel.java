package com.hammad.iphoneringtones.ui.wallpapers;

import android.content.Context;
import android.net.Uri;
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
    private SingleLiveEvent<ArrayList<WallpaperModel>> featuresMutableLiveData;

    public WallpapersViewModel() {

    }

    public SingleLiveEvent<ArrayList<WallpaperModel>> setFeaturesData(Context context) {
        if (featuresMutableLiveData == null) {
            featuresMutableLiveData = new SingleLiveEvent<>();
            featuresMutableLiveData.setValue(getFeaturesList(context));
        }
        return featuresMutableLiveData;
    }

    private ArrayList<WallpaperModel> getFeaturesList(Context context) {
        ArrayList<WallpaperModel> featuresArrayList = new ArrayList<>();
        for (int i = 0; i < Categories.getFeatures(context).size(); i++) {
            WallpaperModel wallpaperModel = new WallpaperModel();
            wallpaperModel.setWallpaperTitle(Categories.getFeatures(context).get(i));
            wallpaperModel.setImage(Categories.getFeatureImages(context).getResourceId(i, 0));
            featuresArrayList.add(wallpaperModel);
        }
        return featuresArrayList;
    }

    public SingleLiveEvent<ArrayList<WallpaperModel>> getPopularData() {
        if (arrayListMutableLiveData == null) {
            arrayListMutableLiveData = new SingleLiveEvent<>();
            Log.d("TAG", "getPopularData: ");
            loadPopularWallpaper();
        }
        return arrayListMutableLiveData;
    }

    private void loadPopularWallpaper() {
        DatabaseReference wallpapersRef = FirebaseDatabase.getInstance().getReference().child("wallpapers");
        wallpapersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<WallpaperModel> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshot.child(Objects.requireNonNull(dataSnapshot.getKey())).getRef().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            WallpaperModel wallpaperModel = new WallpaperModel();
                            wallpaperModel.setWallpaperUri(Objects.requireNonNull(dataSnapshot.child("imageUrl").getValue()).toString());
                            list.add(wallpaperModel);
                            arrayListMutableLiveData.setValue(list);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
//                arrayListMutableLiveData.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void retrieveWallpapers() {
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
                    ringtoneModelArrayList.add(new WallpaperModel(item.getName(), uri.toString()));
                    Log.d(TAG, "onSuccess: "
                            + "\n | file: " + FirebaseStorage.getInstance().getReferenceFromUrl(uri.toString()).getName()
                            + "\n | key: " + ringtonesRef.push().getKey()
                            + "\n | uri:  " + uri);
                });
            }
            arrayListMutableLiveData.setValue(ringtoneModelArrayList);
        }).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.getMessage()));
    }

    public SingleLiveEvent<WallpaperModel> getPopularData1() {
        if (wallpaperModelSingleLiveEvent == null) {
            wallpaperModelSingleLiveEvent = new SingleLiveEvent<>();
            loadPopularWallpaper1();
        }
        return wallpaperModelSingleLiveEvent;
    }

    private void loadPopularWallpaper1() {
        DatabaseReference wallpapersRef = FirebaseDatabase.getInstance().getReference().child("wallpapers");
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

    public void retrieveWallpapers1() {
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
                    Log.d(TAG, "onSuccess: "
                            + "\n | file: " + FirebaseStorage.getInstance().getReferenceFromUrl(uri.toString()).getName()
                            + "\n | key: " + ringtonesRef.push().getKey()
                            + "\n | uri:  " + uri);
                });
            }
        }).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.getMessage()));
    }
}