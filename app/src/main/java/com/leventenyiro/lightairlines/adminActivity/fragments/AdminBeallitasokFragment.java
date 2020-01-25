package com.leventenyiro.lightairlines.adminActivity.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.globalActivity.PasswordUpdate;

public class AdminBeallitasokFragment extends Fragment implements View.OnClickListener {

    private Button btnPasswordUpdate, btnLogout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_admin_beallitasok, container, false);
        init(root);
        btnLogout.setOnClickListener(this);
        btnPasswordUpdate.setOnClickListener(this);
        return root;
    }

    private void init(View root) {
        btnPasswordUpdate = root.findViewById(R.id.btnPasswordUpdate);
        btnLogout = root.findViewById(R.id.btnLogout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogout:
                getActivity().onBackPressed();
                break;
            case R.id.btnPasswordUpdate:
                getActivity().getSharedPreferences("variables", Context.MODE_PRIVATE).edit().putString("fragment", "beallitasok").apply();
                Intent intent = new Intent(getActivity(), PasswordUpdate.class);
                startActivity(intent);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}