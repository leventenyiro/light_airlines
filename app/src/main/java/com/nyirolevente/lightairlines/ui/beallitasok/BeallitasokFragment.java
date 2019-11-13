package com.nyirolevente.lightairlines.ui.beallitasok;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.nyirolevente.lightairlines.R;

public class BeallitasokFragment extends Fragment {

    private BeallitasokViewModel beallitasokViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        beallitasokViewModel =
                ViewModelProviders.of(this).get(BeallitasokViewModel.class);
        View root = inflater.inflate(R.layout.fragment_jaratok, container, false);
        return root;
    }
}