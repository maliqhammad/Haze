package com.hammad.haze.ui.ringtones;

import static com.hammad.haze.classes.NetworkConnectivity.isInternetConnected;

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
import com.hammad.haze.classes.DialogProgressBar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class RingtonesViewModel extends ViewModel {
    private static final String TAG = "RingtonesViewModel";
    FirebaseStorage firebaseStorage;
    StorageReference pathReference;
    MutableLiveData<RingtoneModel> ringtoneModelMutableLiveData;
    DialogProgressBar progressBar;

    public RingtonesViewModel() {

    }

    public MutableLiveData<RingtoneModel> getRingtones(Context context) {
        Log.d(TAG, "getRingtones: ");
        progressBar = new DialogProgressBar(context);
        if (ringtoneModelMutableLiveData == null) {
            ringtoneModelMutableLiveData = new MutableLiveData<>();
        }
        if (isInternetConnected(context)) {
            progressBar.showSpinnerDialog();
            loadRingTones();
        }
        return ringtoneModelMutableLiveData;
    }

    private void loadRingTones() {
        DatabaseReference wallpapersRef = FirebaseDatabase.getInstance().getReference().child("ringtones");
        wallpapersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshot.child(Objects.requireNonNull(dataSnapshot.getKey())).getRef().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            progressBar.cancelSpinnerDialog();
                            ringtoneModelMutableLiveData.setValue(new RingtoneModel(
                                    Objects.requireNonNull(dataSnapshot.child("ringtoneTitle").getValue()).toString(),
                                    Objects.requireNonNull(dataSnapshot.child("ringtoneUrl").getValue()).toString()
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

    public void retrieveRingtones(Context context) {
        progressBar = new DialogProgressBar(context);
        if (isInternetConnected(context)) {
            progressBar.showSpinnerDialog();
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
                        progressBar.cancelSpinnerDialog();
                    });
                }
            }).addOnFailureListener(e -> progressBar.cancelSpinnerDialog());
        }
    }

}