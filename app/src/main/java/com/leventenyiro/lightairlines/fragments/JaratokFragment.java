package com.leventenyiro.lightairlines.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.leventenyiro.lightairlines.segedOsztalyok.Database;
import com.leventenyiro.lightairlines.JaratActivity;
import com.leventenyiro.lightairlines.R;

import java.util.ArrayList;
import java.util.List;

public class JaratokFragment extends Fragment {

    private Context mContext;
    private Database db;
    private EditText inputHonnan, inputHova;
    private List<Integer> cardLista;
    private RelativeLayout mRelativeLayout;
    private SharedPreferences s;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_jaratok, container, false);
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
        s = getActivity().getSharedPreferences("variables", Context.MODE_PRIVATE);
    }

    public void select() {
        for (int i : cardLista) {
            CardView c = mRelativeLayout.findViewById(i);
            mRelativeLayout.removeView(c);
        }
        cardLista.clear();

        Cursor eredmeny = db.selectJaratok(inputHonnan.getText().toString(), inputHova.getText().toString(), s.getString("userId", ""));
        String jaratId;
        String helyekSzama;
        String idopont;
        String indulas;
        String celallomas;
        String idotartam;
        if (eredmeny != null && eredmeny.getCount() > 0) {
            int id = 0;
            while (eredmeny.moveToNext()) {
                jaratId = eredmeny.getString(0);
                helyekSzama = eredmeny.getString(1);
                idopont = eredmeny.getString(2);
                indulas = eredmeny.getString(3);
                celallomas = eredmeny.getString(4);
                idotartam = eredmeny.getString(5);

                CardView card = new CardView(mContext);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dpToPx(360), dpToPx(200));
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                if (cardLista.size() == 0)
                    params.addRule(RelativeLayout.BELOW, R.id.inputHova);
                else
                    params.addRule(RelativeLayout.BELOW, id);
                if (eredmeny.getCount() - 1 == cardLista.size())
                    params.setMargins(0,0,0,dpToPx(100));
                else
                    params.setMargins(0,0,0,dpToPx(20));
                card.setLayoutParams(params);
                card.setCardElevation(50);
                card.setBackground(getResources().getDrawable(R.drawable.bg_card));
                final String finalJaratId = jaratId;
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        s.edit().putString("jaratId", finalJaratId).apply();
                        Intent intent = new Intent(getActivity(), JaratActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                });
                card.setId(card.generateViewId());
                id = card.getId();
                cardLista.add(id);

                RelativeLayout rlCard = new RelativeLayout(mContext);
                RelativeLayout.LayoutParams paramsRlCard = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                rlCard.setLayoutParams(paramsRlCard);

                TextView tvVaros = new TextView(mContext);
                RelativeLayout.LayoutParams paramsVaros = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                paramsVaros.topMargin = dpToPx(20);
                tvVaros.setLayoutParams(paramsVaros);
                tvVaros.setText(indulas + " - " + celallomas);
                tvVaros.setTypeface(getActivity().getResources().getFont(R.font.regular));
                tvVaros.setTextColor(getActivity().getResources().getColor(R.color.gray));
                tvVaros.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvVaros.setId(tvVaros.generateViewId());
                tvVaros.setTextSize(dpToPx(10));

                TextView tvIdopont = new TextView(mContext);
                RelativeLayout.LayoutParams paramsIdopont = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                paramsIdopont.addRule(RelativeLayout.BELOW, tvVaros.getId());
                paramsIdopont.topMargin = dpToPx(15);
                tvIdopont.setLayoutParams(paramsIdopont);
                tvIdopont.setText(idopont.substring(0, 16).replace('-', '.'));
                tvIdopont.setTypeface(getActivity().getResources().getFont(R.font.regular));
                tvIdopont.setTextColor(getActivity().getResources().getColor(R.color.gray));
                tvIdopont.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvIdopont.setId(tvIdopont.generateViewId());
                tvIdopont.setTextSize(dpToPx(7));

                TextView tvIdotartam = new TextView(mContext);
                RelativeLayout.LayoutParams paramsIdotartam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                paramsIdotartam.addRule(RelativeLayout.BELOW, tvIdopont.getId());
                paramsIdotartam.topMargin = dpToPx(15);
                tvIdotartam.setLayoutParams(paramsIdotartam);
                tvIdotartam.setText(idotartamAtalakitas(idotartam));
                tvIdotartam.setTypeface(getActivity().getResources().getFont(R.font.regular));
                tvIdotartam.setTextColor(getActivity().getResources().getColor(R.color.gray));
                tvIdotartam.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvIdotartam.setId(tvIdotartam.generateViewId());
                tvIdotartam.setTextSize(dpToPx(7));

                TextView tvHelyekSzama = new TextView(mContext);
                RelativeLayout.LayoutParams paramsHelyek = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                paramsHelyek.addRule(RelativeLayout.BELOW, tvIdotartam.getId());
                paramsHelyek.topMargin = dpToPx(20);
                tvHelyekSzama.setLayoutParams(paramsHelyek);
                tvHelyekSzama.setText("Már csak " + helyekSzama + " elérhető hely áll rendelkezésre");
                tvHelyekSzama.setTypeface(getActivity().getResources().getFont(R.font.regular));
                tvHelyekSzama.setTextColor(getActivity().getResources().getColor(R.color.gray));
                tvHelyekSzama.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvHelyekSzama.setTextSize(dpToPx(5));

                rlCard.addView(tvVaros);
                rlCard.addView(tvIdopont);
                rlCard.addView(tvIdotartam);
                rlCard.addView(tvHelyekSzama);
                card.addView(rlCard);
                mRelativeLayout.addView(card);
            }
        }
        else {
            TextView tv = new TextView(mContext);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, R.id.inputHova);
            params.topMargin = dpToPx(40);
            tv.setLayoutParams(params);
            tv.setTypeface(getActivity().getResources().getFont(R.font.regular));
            tv.setTextColor(getActivity().getResources().getColor(R.color.gray));
            tv.setTextSize(dpToPx(15));
            tv.setText("Nincs ilyen járat!");
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            mRelativeLayout.addView(tv);
        }
    }

    public int dpToPx(int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()));
    }

    public String idotartamAtalakitas(String idotartam) {
        String[] idoresz = idotartam.split(":");
        if (Integer.parseInt(idoresz[0]) < 10) {
            return idoresz[0].substring(1, 2) + " óra " + idoresz[1] + " perc";
        }
        else {
            return idoresz[0] + " óra " + idoresz[1] + " perc";
        }
    }
}