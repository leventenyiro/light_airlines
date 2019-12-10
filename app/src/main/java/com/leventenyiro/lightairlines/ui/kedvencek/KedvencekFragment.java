package com.leventenyiro.lightairlines.ui.kedvencek;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.leventenyiro.lightairlines.R;

public class KedvencekFragment extends Fragment {

    private KedvencekViewModel kedvencekViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        kedvencekViewModel =
                ViewModelProviders.of(this).get(KedvencekViewModel.class);
        View root = inflater.inflate(R.layout.fragment_kedvencek, container, false);
        return root;
    }
}