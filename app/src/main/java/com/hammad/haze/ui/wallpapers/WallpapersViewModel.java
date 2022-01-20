package com.hammad.haze.ui.wallpapers;

import static com.hammad.haze.classes.NetworkConnectivity.isInternetConnected;

import android.content.Context;
import android.os.Build;
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
import com.hammad.haze.classes.DialogProgressBar;
import com.hammad.haze.classes.SingleLiveEvent;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class WallpapersViewModel extends ViewModel {
    private static final String TAG = "WallpapersViewModel";
    FirebaseStorage firebaseStorage;
    StorageReference pathReference;
    SingleLiveEvent<WallpaperModel> wallpaperModelSingleLiveEvent;
    SingleLiveEvent<WallpaperModel> categoryMutableLiveData;
    Callback callback;
    DialogProgressBar progressBar;

    public SingleLiveEvent<WallpaperModel> getCategoryList(Context context) {
        progressBar = new DialogProgressBar(context);
        categoryMutableLiveData = new SingleLiveEvent<>();
        if (isInternetConnected(context)) {
            progressBar.showSpinnerDialog();
            loadCategoryList();
        }
        return categoryMutableLiveData;
    }

    private void loadCategoryList() {
        DatabaseReference wallpapersRef = FirebaseDatabase.getInstance().getReference().child("categories");
        wallpapersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Log.d(TAG, "onDataChange: loadCategoryList " + snapshot.getChildrenCount());
                    if (snapshot.getChildrenCount() == 0) {
                        progressBar.cancelSpinnerDialog();
                    }
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshot.child(Objects.requireNonNull(dataSnapshot.getKey())).getRef().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            progressBar.cancelSpinnerDialog();
                            categoryMutableLiveData.setValue(new WallpaperModel(
                                    Objects.requireNonNull(dataSnapshot.child("categoryTitle").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("categoryTitle").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("categoryUrl").getValue()).toString()
                            ));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressBar.cancelSpinnerDialog();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                progressBar.cancelSpinnerDialog();
            }
        });
    }

    public void retrieveCategories(Context context) {
        progressBar = new DialogProgressBar(context);
        if (isInternetConnected(context)) {
            progressBar.showSpinnerDialog();
            DatabaseReference ringtonesRef = FirebaseDatabase.getInstance().getReference();
            firebaseStorage = FirebaseStorage.getInstance();
            pathReference = FirebaseStorage.getInstance().getReference().child("categories");
            pathReference.listAll().addOnSuccessListener(listResult -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Log.d(TAG, "onDataChange: retrieveCategories " + listResult.getItems().size());
                    if (listResult.getItems().size() == 0) {
                        progressBar.cancelSpinnerDialog();
                    }
                }
                for (StorageReference item : listResult.getItems()) {
                    item.getDownloadUrl().addOnSuccessListener(uri -> {
                        String key = ringtonesRef.child("categories").getKey();
                        if (key != null) {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("categoryUrl", uri.toString());
                            hashMap.put("categoryTitle", FirebaseStorage.getInstance().getReferenceFromUrl(uri.toString()).getName());
                            ringtonesRef.child(key).push().setValue(hashMap);
                        }
                        categoryMutableLiveData.setValue(new WallpaperModel(item.getName(), item.getName(), uri.toString()));
                        progressBar.cancelSpinnerDialog();
                    });
                }
            }).addOnFailureListener(e -> progressBar.cancelSpinnerDialog());
        }
    }

    public SingleLiveEvent<WallpaperModel> getPopularWallpaper(Context context) {
        progressBar = new DialogProgressBar(context);
        wallpaperModelSingleLiveEvent = new SingleLiveEvent<>();
        if (isInternetConnected(context)) {
            progressBar.showSpinnerDialog();
            loadPopularWallpaper();
        }
        return wallpaperModelSingleLiveEvent;
    }

    private void loadPopularWallpaper() {
        DatabaseReference wallpapersRef = FirebaseDatabase.getInstance().getReference().child("wallpapers");
        wallpapersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Log.d(TAG, "onDataChange: loadPopularWallpaper " + snapshot.getChildrenCount());
                    if (snapshot.getChildrenCount() == 0) {
                        progressBar.cancelSpinnerDialog();
                    }
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshot.child(Objects.requireNonNull(dataSnapshot.getKey())).getRef().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            progressBar.cancelSpinnerDialog();
                            wallpaperModelSingleLiveEvent.setValue(new WallpaperModel(
                                    Objects.requireNonNull(dataSnapshot.child("imageTitle").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("imageUrl").getValue()).toString()
                            ));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressBar.cancelSpinnerDialog();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                progressBar.cancelSpinnerDialog();
            }
        });
    }

    public void retrievePopularWallpapers(Context context) {
        progressBar = new DialogProgressBar(context);
        if (isInternetConnected(context)) {
            progressBar.showSpinnerDialog();
            DatabaseReference ringtonesRef = FirebaseDatabase.getInstance().getReference();
            firebaseStorage = FirebaseStorage.getInstance();
            pathReference = FirebaseStorage.getInstance().getReference().child("wallpapers");
            pathReference.listAll().addOnSuccessListener(listResult -> {
                for (StorageReference item : listResult.getItems()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Log.d(TAG, "onDataChange: retrievePopularWallpapers " + listResult.getItems().size());
                        if (listResult.getItems().size() == 0) {
                            progressBar.cancelSpinnerDialog();
                        }
                    }
                    item.getDownloadUrl().addOnSuccessListener(uri -> {
                        String key = ringtonesRef.child("wallpapers").getKey();
                        if (key != null) {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("imageUrl", uri.toString());
                            hashMap.put("imageTitle", FirebaseStorage.getInstance().getReferenceFromUrl(uri.toString()).getName());
                            ringtonesRef.child(key).push().setValue(hashMap);
                        }
                        wallpaperModelSingleLiveEvent.setValue(new WallpaperModel(item.getName(), uri.toString()));
                        progressBar.cancelSpinnerDialog();
                    });
                }
            }).addOnFailureListener(e -> progressBar.cancelSpinnerDialog());
        }
    }

    public SingleLiveEvent<WallpaperModel> getWallpaperByCategory(Context context, WallpaperModel wallpaperModel) {
        progressBar = new DialogProgressBar(context);
        wallpaperModelSingleLiveEvent = new SingleLiveEvent<>();
        if (isInternetConnected(context)) {
            progressBar.showSpinnerDialog();
            loadWallpapersByCategory(wallpaperModel);
        }
        return wallpaperModelSingleLiveEvent;
    }

    private void loadWallpapersByCategory(WallpaperModel wallpaperModel) {
        Log.d(TAG, "loadWallpapersByCategory: " + wallpaperModel.getCategory());
        DatabaseReference wallpapersRef = FirebaseDatabase.getInstance().getReference().child("wallpapersByCategory").child(wallpaperModel.getCategory());
        wallpapersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Log.d(TAG, "onDataChange: loadWallpapersByCategory " + snapshot.getChildrenCount());
                    if (snapshot.getChildrenCount() == 0) {
                        progressBar.cancelSpinnerDialog();
                    }
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshot.child(Objects.requireNonNull(dataSnapshot.getKey())).getRef().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            progressBar.cancelSpinnerDialog();
                            wallpaperModelSingleLiveEvent.setValue(new WallpaperModel(
                                    Objects.requireNonNull(dataSnapshot.child("imageTitle").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("imageUrl").getValue()).toString()
                            ));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressBar.cancelSpinnerDialog();
                            Log.d(TAG, "onCancelled: ");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                progressBar.cancelSpinnerDialog();
            }
        });
    }

    public void retrieveWallpapersByCategory(Context context, WallpaperModel wallpaperModel) {
        Log.d(TAG, "retrieveWallpapersByCategory: " + wallpaperModel.getCategory());
        progressBar = new DialogProgressBar(context);
        if (isInternetConnected(context)) {
            progressBar.showSpinnerDialog();
            DatabaseReference wallpapersByCategoryRef = FirebaseDatabase.getInstance().getReference().child("wallpapersByCategory");
            firebaseStorage = FirebaseStorage.getInstance();
            pathReference = FirebaseStorage.getInstance().getReference().child("wallpapersByCategory").child(wallpaperModel.getCategory());
            pathReference.listAll().addOnSuccessListener(listResult -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Log.d(TAG, "onDataChange: retrieveWallpapersByCategory " + listResult.getItems().size());
                    if (listResult.getItems().size() == 0) {
                        progressBar.cancelSpinnerDialog();
                    }
                }
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
                        progressBar.cancelSpinnerDialog();
                    });
                }
            }).addOnFailureListener(e -> progressBar.cancelSpinnerDialog());
        }
    }

    public void isRetrieveCategory(Callback callback) {
        this.callback = callback;
        DatabaseReference configRef = FirebaseDatabase.getInstance().getReference().child("appConfiguration");
        configRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = Objects.requireNonNull(snapshot.child("isRetrieveCategory").getValue()).toString();
                callback.onDataChange(value.equals("true"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onDataChange(false);
            }
        });
    }

    public interface Callback {
        void onDataChange(boolean isRetrieveCategory);
    }
}