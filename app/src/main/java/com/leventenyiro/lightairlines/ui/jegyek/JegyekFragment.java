package com.leventenyiro.lightairlines.ui.jegyek;

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

public class JegyekFragment extends Fragment {

    private JegyekViewModel jegyekViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        jegyekViewModel =
                ViewModelProviders.of(this).get(JegyekViewModel.class);
        View root = inflater.inflate(R.layout.fragment_jegyek, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        jegyekViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}