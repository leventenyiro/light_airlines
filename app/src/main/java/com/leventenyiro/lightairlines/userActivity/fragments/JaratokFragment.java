package com.leventenyiro.lightairlines.userActivity.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.leventenyiro.lightairlines.userActivity.JaratActivity;
import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.segedOsztaly.Metodus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class JaratokFragment extends Fragment {

    private Context mContext;
    private AutoCompleteTextView inputHonnan, inputHova;
    private int dp5, dp7, dp10, dp15,dp20, dp40, dp100, dp200, dp360;
    private List<Integer> cardLista;
    private Metodus m;
    private RelativeLayout mRelativeLayout;
    private SharedPreferences s;
    private View loading;
    private List<String> varosLista;
    private DatabaseReference ref;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_jaratok, container, false);
        init(root);
        select();
        inputHonnan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputHonnan.showDropDown();
            }
        });
        inputHova.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputHova.showDropDown();
            }
        });
        inputHonnan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (inputHonnan.getText().toString().equals(getString(R.string.search)))
                    inputHonnan.setText("");
                select();
            }
        });
        inputHova.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (inputHova.getText().toString().equals(getString(R.string.search)))
                    inputHova.setText("");
                select();
            }
        });
        return root;
    }

    private void init(View root) {
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
        dp200 = m.dpToPx(200, getResources());
        dp360 = m.dpToPx(360, getResources());
        s = getActivity().getSharedPreferences("variables", Context.MODE_PRIVATE);
        varosLista = new LinkedList<>();
        varosLista.add(getString(R.string.search));
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("airport").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!varosLista.contains(String.valueOf(snapshot.child("nev").getValue())))
                        varosLista.add(String.valueOf(snapshot.child("nev").getValue()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(root.getContext(), android.R.layout.simple_dropdown_item_1line, varosLista);
        inputHonnan.setAdapter(adapter);
        inputHova.setAdapter(adapter);
        loading = root.findViewById(R.id.loading);
    }

    private void select() {
        m.loading(loading);
        for (int i : cardLista) {
            CardView c = mRelativeLayout.findViewById(i);
            mRelativeLayout.removeView(c);
        }
        cardLista.clear();

        Query query = ref.child("jarat");
        if (!inputHonnan.getText().toString().isEmpty() && !inputHova.getText().toString().isEmpty()) {
            query = query.orderByChild("utvonal").equalTo(inputHonnan.getText().toString() + ";" + inputHova.getText().toString());
        }
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int id = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (m.idopontEllenorzes(String.valueOf(snapshot.child("idopont").getValue()))) {
                            CardView card = new CardView(mContext);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dp360, dp200);
                            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                            if (cardLista.size() == 0)
                                params.addRule(RelativeLayout.BELOW, R.id.inputHova);
                            else
                                params.addRule(RelativeLayout.BELOW, id);
                            if (dataSnapshot.getChildrenCount() - 1 == cardLista.size())
                                params.setMargins(0, 0, 0, dp100);
                            else
                                params.setMargins(0, 0, 0, dp20);
                            card.setLayoutParams(params);
                            card.setCardElevation(50);
                            card.setBackground(getResources().getDrawable(R.drawable.card));
                            final String finalJaratId = snapshot.getKey();
                            card.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    s.edit().putString("jaratId", finalJaratId).apply();
                                    Intent intent = new Intent(getActivity(), JaratActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
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
                            paramsVaros.topMargin = dp20;
                            tvVaros.setLayoutParams(paramsVaros);
                            String fromTo = snapshot.child("indulas_nev").getValue() + " - " + snapshot.child("celallomas_nev").getValue();
                            tvVaros.setText(fromTo);
                            tvVaros.setTypeface(getActivity().getResources().getFont(R.font.regular));
                            tvVaros.setTextColor(getActivity().getResources().getColor(R.color.gray));
                            tvVaros.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            tvVaros.setId(tvVaros.generateViewId());
                            tvVaros.setTextSize(dp10);

                            TextView tvIdopont = new TextView(mContext);
                            RelativeLayout.LayoutParams paramsIdopont = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            paramsIdopont.addRule(RelativeLayout.BELOW, tvVaros.getId());
                            paramsIdopont.topMargin = dp15;
                            tvIdopont.setLayoutParams(paramsIdopont);
                            tvIdopont.setText(String.valueOf(snapshot.child("idopont").getValue()).substring(0, 16).replace('-', '.'));
                            tvIdopont.setTypeface(getActivity().getResources().getFont(R.font.regular));
                            tvIdopont.setTextColor(getActivity().getResources().getColor(R.color.gray));
                            tvIdopont.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            tvIdopont.setId(tvIdopont.generateViewId());
                            tvIdopont.setTextSize(dp7);

                            TextView tvIdotartam = new TextView(mContext);
                            RelativeLayout.LayoutParams paramsIdotartam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            paramsIdotartam.addRule(RelativeLayout.BELOW, tvIdopont.getId());
                            paramsIdotartam.topMargin = dp15;
                            tvIdotartam.setLayoutParams(paramsIdotartam);
                            tvIdotartam.setText(m.idotartamAtalakitas(String.valueOf(snapshot.child("idotartam").getValue())));
                            tvIdotartam.setTypeface(getActivity().getResources().getFont(R.font.regular));
                            tvIdotartam.setTextColor(getActivity().getResources().getColor(R.color.gray));
                            tvIdotartam.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            tvIdotartam.setId(tvIdotartam.generateViewId());
                            tvIdotartam.setTextSize(dp7);

                            TextView tvHelyekSzama = new TextView(mContext);
                            RelativeLayout.LayoutParams paramsHelyek = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            paramsHelyek.addRule(RelativeLayout.BELOW, tvIdotartam.getId());
                            paramsHelyek.topMargin = dp20;
                            tvHelyekSzama.setLayoutParams(paramsHelyek);
                            String helyInfo = getString(R.string.seatInfo1) + " " + snapshot.child("helyek_szama").getValue() + " " + getString(R.string.seatInfo2);
                            tvHelyekSzama.setText(helyInfo);
                            tvHelyekSzama.setTypeface(getActivity().getResources().getFont(R.font.regular));
                            tvHelyekSzama.setTextColor(getActivity().getResources().getColor(R.color.gray));
                            tvHelyekSzama.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            tvHelyekSzama.setTextSize(dp5);

                            rlCard.addView(tvVaros);
                            rlCard.addView(tvIdopont);
                            rlCard.addView(tvIdotartam);
                            rlCard.addView(tvHelyekSzama);
                            card.addView(rlCard);
                            mRelativeLayout.addView(card);
                        } else {
                            TextView tv = new TextView(mContext);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.BELOW, R.id.inputHova);
                            params.topMargin = dp40;
                            tv.setLayoutParams(params);
                            tv.setTypeface(getActivity().getResources().getFont(R.font.regular));
                            tv.setTextColor(getActivity().getResources().getColor(R.color.gray));
                            tv.setTextSize(dp15);
                            tv.setText(getString(R.string.noFlight));
                            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            mRelativeLayout.addView(tv);
                        }
                    }
                } else {
                    TextView tv = new TextView(mContext);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.BELOW, R.id.inputHova);
                    params.topMargin = dp40;
                    tv.setLayoutParams(params);
                    tv.setTypeface(getActivity().getResources().getFont(R.font.regular));
                    tv.setTextColor(getActivity().getResources().getColor(R.color.gray));
                    tv.setTextSize(dp15);
                    tv.setText(getString(R.string.noFlight));
                    tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    mRelativeLayout.addView(tv);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        m.loading(loading);
    }
}