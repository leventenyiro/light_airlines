package com.leventenyiro.lightairlines.ui.jegyek;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JegyekViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public JegyekViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Jegyek");
    }

    public LiveData<String> getText() {
        return mText;
    }
}