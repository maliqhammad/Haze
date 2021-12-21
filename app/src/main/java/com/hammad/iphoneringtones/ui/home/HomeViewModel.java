package com.hammad.iphoneringtones.ui.home;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hammad.iphoneringtones.ui.wallpapers.WallpaperModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class HomeViewModel extends ViewModel {
    //private static final String TAG = "HomeViewModel";
    MutableLiveData<ArrayList<WallpaperModel>> arrayListMutableLiveData;
    private MutableLiveData<ArrayList<WallpaperModel>> featuresMutableLiveData;

    public HomeViewModel() {

    }

    public LiveData<ArrayList<WallpaperModel>> setFeaturesData(Context context) {
        if (featuresMutableLiveData == null) {
            featuresMutableLiveData = new MutableLiveData<>();
            featuresMutableLiveData.setValue(getFeaturesList(context));
        }
        return featuresMutableLiveData;
    }

    private ArrayList<WallpaperModel> getFeaturesList(Context context) {
        ArrayList<WallpaperModel> featuresArrayList = new ArrayList<>();
        for (int i = 0; i < Categories.getFeatures(context).size(); i++) {
            WallpaperModel wallpaperModel = new WallpaperModel();
            wallpaperModel.setImageName(Categories.getFeatures(context).get(i));
            wallpaperModel.setImage(Categories.getFeatureImages(context).getResourceId(i, 0));
            featuresArrayList.add(wallpaperModel);
        }
        return featuresArrayList;
    }

    public LiveData<ArrayList<WallpaperModel>> getPopularData() {
        if (arrayListMutableLiveData == null) {
            arrayListMutableLiveData = new MutableLiveData<>();
            Log.d("TAG", "getPopularData: ");
            loadPopularWallpaper();
        }
        return arrayListMutableLiveData;
    }

    public LiveData<ArrayList<WallpaperModel>> setPopularData(ArrayList<WallpaperModel> arrayList) {
        Log.d("TAG", "setPopularData: " + arrayList.size());
        if (arrayListMutableLiveData == null) {
            arrayListMutableLiveData = new MutableLiveData<>();
            arrayListMutableLiveData.setValue(arrayList);
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
                            wallpaperModel.setUri(Uri.parse(Objects.requireNonNull(dataSnapshot.child("imageUrl").getValue()).toString()));
                            list.add(wallpaperModel);
                            arrayListMutableLiveData.setValue(list);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                arrayListMutableLiveData.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

//    private ArrayList<WallpaperModel> setPopularList() {
//        ArrayList<WallpaperModel> arrayList = new ArrayList<>();
//
//        WallpaperModel wallpaperModel0 = new WallpaperModel();
//        wallpaperModel0.setImage(R.drawable.image_1);
//        wallpaperModel0.setImageName("Image " + 0);
//        arrayList.add(wallpaperModel0);
//
//        WallpaperModel wallpaperModel1 = new WallpaperModel();
//        wallpaperModel1.setImage(R.drawable.image_2);
//        wallpaperModel1.setImageName("Image " + 1);
//        arrayList.add(wallpaperModel1);
//
//        WallpaperModel wallpaperModel2 = new WallpaperModel();
//        wallpaperModel2.setImage(R.drawable.image_3);
//        wallpaperModel2.setImageName("Image " + 2);
//        arrayList.add(wallpaperModel2);
//
//        WallpaperModel wallpaperModel3 = new WallpaperModel();
//        wallpaperModel3.setImage(R.drawable.image_4);
//        wallpaperModel3.setImageName("Image " + 3);
//        arrayList.add(wallpaperModel3);
//
//        WallpaperModel wallpaperModel4 = new WallpaperModel();
//        wallpaperModel4.setImage(R.drawable.image_5);
//        wallpaperModel4.setImageName("Image " + 4);
//        arrayList.add(wallpaperModel4);
//
//        WallpaperModel wallpaperModel5 = new WallpaperModel();
//        wallpaperModel5.setImage(R.drawable.image_6);
//        wallpaperModel5.setImageName("Image " + 5);
//        arrayList.add(wallpaperModel5);
//
//        WallpaperModel wallpaperModel6 = new WallpaperModel();
//        wallpaperModel6.setImage(R.drawable.image_7);
//        wallpaperModel6.setImageName("Image " + 6);
//        arrayList.add(wallpaperModel6);
//
//        WallpaperModel wallpaperModel7 = new WallpaperModel();
//        wallpaperModel7.setImage(R.drawable.image_8);
//        wallpaperModel7.setImageName("Image " + 7);
//        arrayList.add(wallpaperModel7);
//
//        WallpaperModel wallpaperModel8 = new WallpaperModel();
//        wallpaperModel8.setImage(R.drawable.image_9);
//        wallpaperModel8.setImageName("Image " + 8);
//        arrayList.add(wallpaperModel8);
//
//        WallpaperModel wallpaperModel9 = new WallpaperModel();
//        wallpaperModel9.setImage(R.drawable.image_10);
//        wallpaperModel9.setImageName("Image " + 9);
//        arrayList.add(wallpaperModel9);
//
//        arrayList.addAll(arrayList);
//        arrayList.addAll(arrayList);
//        arrayList.addAll(arrayList);
//        arrayList.addAll(arrayList);
//
//        return arrayList;
//    }
}