package com.hammad.iphoneringtones.ui.ringtones;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
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

public class RingtonesViewModel extends ViewModel {

    private static final String TAG = "RingtonesViewModel";
    MutableLiveData<String> mText;
    FirebaseStorage firebaseStorage;
    StorageReference pathReference;
    MutableLiveData<ArrayList<RingtoneModel>> arrayListMutableLiveData;
    ArrayList<RingtoneModel> ringtoneModelArrayList;
    MutableLiveData<RingtoneModel> ringtoneModelMutableLiveData;
    RingtoneModel ringtoneModel;

    public RingtonesViewModel(Context context) {
        arrayListMutableLiveData = new MutableLiveData<>();
        loadRingTones();
        arrayListMutableLiveData.setValue(ringtoneModelArrayList);
    }

    public MutableLiveData<ArrayList<RingtoneModel>> getRingtones() {
        Log.d(TAG, "getRingtones: ");
        return arrayListMutableLiveData;
    }

    private void loadRingTones() {
        ringtoneModelArrayList = new ArrayList<>();
        DatabaseReference wallpapersRef = FirebaseDatabase.getInstance().getReference().child("ringtones");
        wallpapersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshot.child(Objects.requireNonNull(dataSnapshot.getKey())).getRef().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d(TAG, "onDataChange: " + Objects.requireNonNull(dataSnapshot.child("ringtoneUrl").getValue()).toString());
                            ringtoneModelArrayList.add(new RingtoneModel(
                                    Objects.requireNonNull(dataSnapshot.child("ringtoneTitle").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("ringtoneUrl").getValue()).toString()
                            ));
                            arrayListMutableLiveData.setValue(ringtoneModelArrayList);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
//                arrayListMutableLiveData.setValue(ringtoneModelArrayList);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void retrieveRingtones() {
        DatabaseReference ringtonesRef = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        pathReference = FirebaseStorage.getInstance().getReference().child("ringtones");
        pathReference.listAll().addOnSuccessListener(listResult -> {
            ArrayList<RingtoneModel> ringtoneModelArrayList = new ArrayList<>();
            for (StorageReference item : listResult.getItems()) {
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    String key = ringtonesRef.child("ringtones").getKey();
                    if (key != null) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("ringtoneUrl", uri.toString());
                        hashMap.put("ringtoneTitle", FirebaseStorage.getInstance().getReferenceFromUrl(uri.toString()).getName());
                        ringtonesRef.child(key).push().setValue(hashMap);
                    }
                    ringtoneModelArrayList.add(new RingtoneModel(item.getName(), String.valueOf(uri)));
                    Log.d(TAG, "onSuccess: "
                            + "\n | file: " + FirebaseStorage.getInstance().getReferenceFromUrl(uri.toString()).getName()
                            + "\n | key: " + ringtonesRef.push().getKey()
                            + "\n | uri:  " + uri);
                });
            }
            arrayListMutableLiveData.setValue(ringtoneModelArrayList);
        }).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.getMessage()));
    }

    public RingtonesViewModel() {

    }

    public MutableLiveData<RingtoneModel> getRingtones1() {
        Log.d(TAG, "getRingtones: ");
        if (ringtoneModelMutableLiveData == null) {
            ringtoneModelMutableLiveData = new MutableLiveData<>();
        }
        loadRingTones1();
        return ringtoneModelMutableLiveData;
    }

    private void loadRingTones1() {
        DatabaseReference wallpapersRef = FirebaseDatabase.getInstance().getReference().child("ringtones");
        wallpapersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshot.child(Objects.requireNonNull(dataSnapshot.getKey())).getRef().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d(TAG, "onDataChange: " + Objects.requireNonNull(dataSnapshot.child("ringtoneUrl").getValue()).toString());
                            ringtoneModelMutableLiveData.setValue(new RingtoneModel(
                                    Objects.requireNonNull(dataSnapshot.child("ringtoneTitle").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("ringtoneUrl").getValue()).toString()
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

    public void retrieveRingtones1() {
        DatabaseReference ringtonesRef = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        pathReference = FirebaseStorage.getInstance().getReference().child("ringtones");
        pathReference.listAll().addOnSuccessListener(listResult -> {
            ArrayList<RingtoneModel> ringtoneModelArrayList = new ArrayList<>();
            for (StorageReference item : listResult.getItems()) {
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    String key = ringtonesRef.child("ringtones").getKey();
                    if (key != null) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("ringtoneUrl", uri.toString());
                        hashMap.put("ringtoneTitle", FirebaseStorage.getInstance().getReferenceFromUrl(uri.toString()).getName());
                        ringtonesRef.child(key).push().setValue(hashMap);
                    }
                    ringtoneModelMutableLiveData.setValue(new RingtoneModel(item.getName(), String.valueOf(uri)));
                    Log.d(TAG, "onSuccess: "
                            + "\n | file: " + FirebaseStorage.getInstance().getReferenceFromUrl(uri.toString()).getName()
                            + "\n | key: " + ringtonesRef.push().getKey()
                            + "\n | uri:  " + uri);
                });
            }
        }).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e.getMessage()));
    }

    public MutableLiveData<String> getText() {
        return mText;
    }
}