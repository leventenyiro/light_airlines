package com.leventenyiro.lightairlines.userActivity.fragments;

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
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leventenyiro.lightairlines.userActivity.JegyActivity;
import com.leventenyiro.lightairlines.R;
import com.leventenyiro.lightairlines.segedOsztaly.Database;
import com.leventenyiro.lightairlines.segedOsztaly.Metodus;

import java.util.ArrayList;
import java.util.List;

public class JegyekFragment extends Fragment {

    private Context mContext;
    private DatabaseReference db;
    private int dp7, dp8, dp10, dp11, dp15, dp20, dp100, dp200, dp360;
    private List<Integer> cardLista;
    private Metodus m;
    private RelativeLayout mRelativeLayout;
    private SharedPreferences s;
    private List<Integer> id;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_jegyek, container, false);
        init(root);
        select();
        return root;
    }

    private void init(View root) {
        db = FirebaseDatabase.getInstance().getReference();
        cardLista = new ArrayList<>();
        mContext = root.getContext();
        mRelativeLayout = root.findViewById(R.id.relativeLayout);
        m = new Metodus(getActivity());
        dp7 = m.dpToPx(7, getResources());
        dp8 = m.dpToPx(8, getResources());
        dp10 = m.dpToPx(10, getResources());
        dp11 = m.dpToPx(11, getResources());
        dp15 = m.dpToPx(15, getResources());
        dp20 = m.dpToPx(20, getResources());
        dp100 = m.dpToPx(100, getResources());
        dp200 = m.dpToPx(200, getResources());
        dp360 = m.dpToPx(360, getResources());
        s = getActivity().getSharedPreferences("variables", Context.MODE_PRIVATE);
        id = new ArrayList<>();
    }

    private void select() {
        for (int i : cardLista) {
            CardView c = mRelativeLayout.findViewById(i);
            mRelativeLayout.removeView(c);
        }
        cardLista.clear();

        db.child("foglalas").orderByChild("user_id").equalTo(Integer.parseInt(s.getString("userId", ""))).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final String ules = String.valueOf(snapshot.child("ules").getValue());
                        String foglalasId = String.valueOf(snapshot.getKey());
                        //int id = 0;
                        id.add(0);


                        CardView card = new CardView(mContext);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dp360, dp200);
                        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                        if (cardLista.size() == 0)
                            params.addRule(RelativeLayout.BELOW, R.id.textCim);
                        else
                            params.addRule(RelativeLayout.BELOW, id.get(0));
                        if (dataSnapshot.getChildrenCount() - 1 == cardLista.size())
                            params.setMargins(0, 0, 0, dp100);
                        else
                            params.setMargins(0, 0, 0, dp20);
                        card.setLayoutParams(params);
                        card.setCardElevation(50);
                        card.setBackground(getResources().getDrawable(R.drawable.card));
                        final String finalFoglalasId = foglalasId;
                        card.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                s.edit().putString("foglalasId", finalFoglalasId).apply();
                                s.edit().putString("fragment", "jegyek").apply();
                                Intent intent = new Intent(getActivity(), JegyActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        });

                        card.setId(card.generateViewId());
                        id.set(0, card.getId());
                        cardLista.add(id.get(0));

                        RelativeLayout rlCard = new RelativeLayout(mContext);
                        RelativeLayout.LayoutParams paramsRlCard = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        rlCard.setLayoutParams(paramsRlCard);

                        final TextView tvVaros = new TextView(mContext);
                        final TextView tvIdopont = new TextView(mContext);
                        final TextView tvIdotartam = new TextView(mContext);
                        final TextView tvUles = new TextView(mContext);

                        db.child("jarat").orderByKey().equalTo(String.valueOf(snapshot.child("jarat_id").getValue())).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                DataSnapshot snapshot = dataSnapshot.getChildren().iterator().next();

                                RelativeLayout.LayoutParams paramsVaros = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                paramsVaros.topMargin = dp20;
                                tvVaros.setLayoutParams(paramsVaros);
                                tvVaros.setText(snapshot.child("indulas_nev").getValue() + " - " + snapshot.child("celallomas_nev").getValue());
                                tvVaros.setTypeface(getActivity().getResources().getFont(R.font.regular));
                                tvVaros.setTextColor(getActivity().getResources().getColor(R.color.gray));
                                tvVaros.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                tvVaros.setId(tvVaros.generateViewId());
                                tvVaros.setTextSize(dp10);


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


                                RelativeLayout.LayoutParams paramsUles = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                paramsUles.addRule(RelativeLayout.BELOW, tvIdotartam.getId());
                                paramsUles.topMargin = dp20;
                                tvUles.setLayoutParams(paramsUles);
                                String ulesInfo = getString(R.string.seatInfo) + " " + ules;
                                tvUles.setText(ulesInfo);
                                tvUles.setTypeface(getActivity().getResources().getFont(R.font.regular));
                                tvUles.setTextColor(getActivity().getResources().getColor(R.color.gray));
                                tvUles.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                tvUles.setTextSize(dp8);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                        });

                        rlCard.addView(tvVaros);
                        rlCard.addView(tvIdopont);
                        rlCard.addView(tvIdotartam);
                        rlCard.addView(tvUles);
                        card.addView(rlCard);
                        mRelativeLayout.addView(card);
                    }
                } else {
                    TextView tv = new TextView(mContext);
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                    params.addRule(RelativeLayout.BELOW, R.id.textCim);
                    params.topMargin = dp200;
                    tv.setLayoutParams(params);
                    tv.setTypeface(ResourcesCompat.getFont(getActivity().getApplicationContext(), R.font.regular));
                    tv.setTextColor(getActivity().getResources().getColor(R.color.gray));
                    tv.setTextSize(dp11);
                    tv.setText(getString(R.string.noReserve));
                    tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    mRelativeLayout.addView(tv);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}