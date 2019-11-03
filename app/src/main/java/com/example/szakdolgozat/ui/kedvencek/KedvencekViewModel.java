package com.example.szakdolgozat.ui.kedvencek;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class KedvencekViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public KedvencekViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}