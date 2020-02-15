package com.leventenyiro.lightairlines.userActivity.fragments;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.leventenyiro.lightairlines.segedOsztaly.Database;
import com.leventenyiro.lightairlines.userActivity.JaratActivity;
import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.segedOsztaly.Metodus;

import java.util.ArrayList;
import java.util.List;

public class JaratokFragment extends Fragment {

    private Context mContext;
    private Database db;
    private EditText inputHonnan, inputHova;
    private int dp5, dp7, dp10, dp15,dp20, dp40, dp100, dp200, dp360;
    private List<Integer> cardLista;
    private Metodus m;
    private RelativeLayout mRelativeLayout;
    private SharedPreferences s;
    private View loading;
    private DatabaseReference ref;

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
        ref = FirebaseDatabase.getInstance().getReference();

        loading = root.findViewById(R.id.loading);
    }

    private void select() {
        m.loading(loading);
        for (int i : cardLista) {
            CardView c = mRelativeLayout.findViewById(i);
            mRelativeLayout.removeView(c);
        }
        cardLista.clear();
        //firebase

        /*final List<String> indulasIdList = new ArrayList<>();
        ref.child("airport").orderByChild("nev").equalTo(inputHonnan.getText().toString()).orderByChild("rovidites").equalTo(inputHonnan.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        indulasIdList.add(snapshot.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        final List<String> celallomasIdList = new ArrayList<>();
        ref.child("airport").orderByChild("nev").equalTo(inputHova.getText().toString()).orderByChild("rovidites").equalTo(inputHova.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        celallomasIdList.add(snapshot.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        // keressük meg azokat az útvonalakat, ahol az indulasId és a celallomasId létezik
        List<String> utvonalIdList = new ArrayList<>();
        Query query = ref.child("utvonal");
        if (indulasIdList.size() > 0) {
            for (String s : indulasIdList) {
                query.orderByChild("indulas_id").equalTo(s).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (dataSnapshot)
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            }
        }*/



        ref.child("jarat") //...



        /*ref.child("jarat").orderByChild("utvonal_id").equalTo(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final int[] id = {0};
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final String jaratId = snapshot.getKey();
                        final String helyekSzama = snapshot.child("helyek_szama").getValue().toString();
                        final String idopont = snapshot.child("idopont").getValue().toString();
                        String utvonalId = snapshot.child("utvonal_id").getValue().toString();
                        final long eredmenyekGetCount = dataSnapshot.getChildrenCount();

                        ref.child("utvonal").orderByKey().equalTo(utvonalId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String indulasId = snapshot.child("indulas_id").getValue().toString();
                                    final String celallomasId = snapshot.child("celallomas_id").getValue().toString();
                                    final String idotartam = snapshot.child("idotartam").getValue().toString();
                                    ref.child("airport").orderByKey().equalTo(indulasId).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                final String indulasNev = snapshot.child("nev").getValue().toString();
                                                String indulasRov = snapshot.child("rovidites").getValue().toString();
                                                ref.child("airport").orderByKey().equalTo(celallomasId).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                            String celallomasNev = snapshot.child("nev").getValue().toString();
                                                            String celallomasRovidites = snapshot.child("rovidites").getValue().toString();

                                                            // KÁRTYÁK IDE
                                                            CardView card = new CardView(mContext);
                                                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dp360, dp200);
                                                            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                                                            if (cardLista.size() == 0)
                                                                params.addRule(RelativeLayout.BELOW, R.id.inputHova);
                                                            else
                                                                params.addRule(RelativeLayout.BELOW, id[0]);
                                                            if (eredmenyekGetCount - 1 == cardLista.size())
                                                                params.setMargins(0,0,0, dp100);
                                                            else
                                                                params.setMargins(0,0,0, dp20);
                                                            card.setLayoutParams(params);
                                                            card.setCardElevation(50);
                                                            card.setBackground(getResources().getDrawable(R.drawable.card));
                                                            final String finalJaratId = jaratId;
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
                                                            id[0] = card.getId();
                                                            cardLista.add(id[0]);

                                                            RelativeLayout rlCard = new RelativeLayout(mContext);
                                                            RelativeLayout.LayoutParams paramsRlCard = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                                            rlCard.setLayoutParams(paramsRlCard);

                                                            TextView tvVaros = new TextView(mContext);
                                                            RelativeLayout.LayoutParams paramsVaros = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                                            paramsVaros.topMargin = dp20;
                                                            tvVaros.setLayoutParams(paramsVaros);
                                                            String fromTo = indulasNev + " - " + celallomasNev;
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
                                                            tvIdopont.setText(idopont.substring(0, 16).replace('-', '.'));
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
                                                            tvIdotartam.setText(m.idotartamAtalakitas(idotartam));
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
                                                            String helyInfo = getString(R.string.seatInfo1) + " " + helyekSzama + " " + getString(R.string.seatInfo2);
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
                                                            m.loading(loading);
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                                                });
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });*/
    }
}