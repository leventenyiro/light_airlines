package com.leventenyiro.lightairlines.adminActivityk.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.segedOsztalyok.Database;
import com.leventenyiro.lightairlines.segedOsztalyok.Metodus;
import com.leventenyiro.lightairlines.userActivityk.JegyActivity;

import java.util.ArrayList;
import java.util.List;

public class JaratokAdminInsertFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_admin_jaratok_insert, container, false);
        init(root);
        return root;
    }

    private void init(View root) {

    }
}