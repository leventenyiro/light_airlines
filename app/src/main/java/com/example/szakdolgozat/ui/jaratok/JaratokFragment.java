package com.example.szakdolgozat.ui.jaratok;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.szakdolgozat.R;

public class JaratokFragment extends Fragment {

    private JaratokViewModel jaratokViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        jaratokViewModel =
                ViewModelProviders.of(this).get(JaratokViewModel.class);
        View root = inflater.inflate(R.layout.fragment_beallitasok, container, false);
        return root;
    }
}