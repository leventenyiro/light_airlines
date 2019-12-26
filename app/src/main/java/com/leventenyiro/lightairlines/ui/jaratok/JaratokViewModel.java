package com.leventenyiro.lightairlines.ui.jaratok;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JaratokViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public JaratokViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Járatok");
    }

    public LiveData<String> getText() {
        return mText;
    }
}