package com.leventenyiro.lightairlines.ui.jaratok;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JaratokViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public JaratokViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}