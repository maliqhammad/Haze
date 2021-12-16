package com.hammad.iphoneringtones.ui.wallpapers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WallpapersViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WallpapersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}