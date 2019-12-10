package com.leventenyiro.lightairlines.ui.foglaltak;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.leventenyiro.lightairlines.R;

public class FoglaltakFragment extends Fragment {

    private FoglaltakViewModel foglaltakViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        foglaltakViewModel =
                ViewModelProviders.of(this).get(FoglaltakViewModel.class);
        View root = inflater.inflate(R.layout.fragment_foglaltak, container, false);
        return root;
    }
}