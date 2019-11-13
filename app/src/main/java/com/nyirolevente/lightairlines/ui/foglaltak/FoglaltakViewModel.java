package com.nyirolevente.lightairlines.ui.foglaltak;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FoglaltakViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FoglaltakViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is send fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}