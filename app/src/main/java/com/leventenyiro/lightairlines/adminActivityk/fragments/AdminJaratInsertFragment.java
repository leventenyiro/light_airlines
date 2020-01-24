package com.leventenyiro.lightairlines.adminActivityk.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.segedOsztalyok.Database;
import com.leventenyiro.lightairlines.segedOsztalyok.Metodus;

import java.util.ArrayList;
import java.util.List;

public class AdminJaratInsertFragment extends Fragment {

    private Context mContext;
    private Database db;
    private EditText inputHonnan, inputHova;
    private int dp5, dp7, dp10, dp15,dp20, dp40, dp100, dp200, dp360;
    private List<Integer> cardLista;
    private Metodus m;
    private RelativeLayout mRelativeLayout;
    private SharedPreferences s;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_admin_jarat_insert, container, false);
        init(root);
        select();
        inputHonnan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                select();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        inputHova.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                select();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        return root;
    }

    private void init(View root) {
        db = new Database(getActivity());
        inputHonnan = root.findViewById(R.id.inputHonnan);
        inputHova = root.findViewById(R.id.inputHova);
        cardLista = new ArrayList<>();
        mContext = root.getContext();
        mRelativeLayout = root.findViewById(R.id.relativeLayout);
        m = new Metodus(getActivity());
        dp5 = m.dpToPx(5, getResources());
        dp7 = m.dpToPx(7, getResources());
        dp10 = m.dpToPx(10, getResources());
        dp15 = m.dpToPx(15, getResources());
        dp20 = m.dpToPx(20, getResources());
        dp40 = m.dpToPx(40, getResources());
        dp100 = m.dpToPx(100, getResources());
        dp360 = m.dpToPx(360, getResources());
        s = getActivity().getSharedPreferences("variables", Context.MODE_PRIVATE);
    }

    private void select() {
        for (int i : cardLista) {
            CardView c = mRelativeLayout.findViewById(i);
            mRelativeLayout.removeView(c);
        }
        cardLista.clear();

        Cursor eredmeny = db.selectUtvonalak(inputHonnan.getText().toString().trim(), inputHova.getText().toString().trim());
        int id = 0;
        if (eredmeny != null && eredmeny.getCount() > 0) {
            while (eredmeny.moveToNext()) {
                final String utvonalId = eredmeny.getString(0);
                String indulasNev = eredmeny.getString(1);
                String indulasRovidites = eredmeny.getString(2);
                String celallomasNev = eredmeny.getString(3);
                String celallomasRovidites = eredmeny.getString(4);
                String idotartam = eredmeny.getString(5);

                CardView card = new CardView(mContext);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dp360, dp100);
                if (cardLista.size() == 0)
                    params.addRule(RelativeLayout.BELOW, R.id.inputHova);
                else
                    params.addRule(RelativeLayout.BELOW, id);
                if (eredmeny.getCount() - 1 == cardLista.size())
                    params.setMargins(0,0,0, dp100);
                else
                    params.setMargins(0,0,0, dp20);
                card.setLayoutParams(params);
                card.setCardElevation(50);
                card.setBackground(getResources().getDrawable(R.drawable.card));
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        s.edit().putString("utvonalId", utvonalId).apply();
                        //Intent intent = new Intent(getActivity(), JaratInsertActivity.class);
                        //startActivity(intent);
                        //getActivity().finish();
                        //getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                });
                card.setId(card.generateViewId());
                id = card.getId();
                cardLista.add(id);

                RelativeLayout rlCard = new RelativeLayout(mContext);
                RelativeLayout.LayoutParams paramsRlCard = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                rlCard.setLayoutParams(paramsRlCard);

                TextView tvRovidites = new TextView(mContext);
                RelativeLayout.LayoutParams paramsRovidites = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                paramsRovidites.topMargin = dp20;
                tvRovidites.setLayoutParams(paramsRovidites);
                String fromTo = indulasRovidites + " - " + celallomasRovidites;
                tvRovidites.setText(fromTo);
                tvRovidites.setTypeface(getActivity().getResources().getFont(R.font.regular));
                tvRovidites.setTextColor(getActivity().getResources().getColor(R.color.gray));
                tvRovidites.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvRovidites.setId(tvRovidites.generateViewId());
                tvRovidites.setTextSize(dp15);

                TextView tvVaros = new TextView(mContext);
                RelativeLayout.LayoutParams paramsVaros = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                paramsVaros.addRule(RelativeLayout.BELOW, tvRovidites.getId());
                paramsVaros.topMargin = dp20;
                tvVaros.setLayoutParams(paramsVaros);
                fromTo = indulasNev + " - " + celallomasNev;
                tvVaros.setText(fromTo);
                tvVaros.setTypeface(getActivity().getResources().getFont(R.font.regular));
                tvVaros.setTextColor(getActivity().getResources().getColor(R.color.gray));
                tvVaros.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvVaros.setId(tvVaros.generateViewId());
                tvVaros.setTextSize(dp10);

                TextView tvIdotartam = new TextView(mContext);
                RelativeLayout.LayoutParams paramsIdotartam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                paramsVaros.addRule(RelativeLayout.BELOW, tvVaros.getId());
                paramsIdotartam.topMargin = dp20;
                tvIdotartam.setLayoutParams(paramsIdotartam);
                tvIdotartam.setText(idotartam);
                tvIdotartam.setTypeface(getActivity().getResources().getFont(R.font.regular));
                tvIdotartam.setTextColor(getActivity().getResources().getColor(R.color.gray));
                tvIdotartam.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvIdotartam.setId(tvIdotartam.generateViewId());
                tvIdotartam.setTextSize(dp10);

                rlCard.addView(tvRovidites);
                rlCard.addView(tvVaros);
                rlCard.addView(tvIdotartam);
                card.addView(rlCard);
                mRelativeLayout.addView(card);
            }
        }
        else {
            TextView tv = new TextView(mContext);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, R.id.inputHova);
            params.topMargin = dp40;
            tv.setLayoutParams(params);
            tv.setTypeface(getActivity().getResources().getFont(R.font.regular));
            tv.setTextColor(getActivity().getResources().getColor(R.color.gray));
            tv.setTextSize(dp15);
            tv.setText(getString(R.string.noLine));
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            mRelativeLayout.addView(tv);
        }
    }
}