package com.hammad.iphoneringtones.ui.ringtones;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RingtonesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RingtonesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}