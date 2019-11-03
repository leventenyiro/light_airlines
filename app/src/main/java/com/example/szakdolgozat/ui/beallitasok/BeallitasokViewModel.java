package com.example.szakdolgozat.ui.beallitasok;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BeallitasokViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BeallitasokViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is share fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}