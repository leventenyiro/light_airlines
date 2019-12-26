package com.leventenyiro.lightairlines.ui.beallitasok;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.leventenyiro.lightairlines.R;

public class BeallitasokFragment extends Fragment {

    private BeallitasokViewModel beallitasokViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        beallitasokViewModel =
                ViewModelProviders.of(this).get(BeallitasokViewModel.class);
        View root = inflater.inflate(R.layout.fragment_beallitasok, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        beallitasokViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}