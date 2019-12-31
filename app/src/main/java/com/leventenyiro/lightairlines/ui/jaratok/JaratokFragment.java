package com.leventenyiro.lightairlines.ui.jaratok;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.leventenyiro.lightairlines.Database;
import com.leventenyiro.lightairlines.R;

import java.util.ArrayList;
import java.util.List;

public class JaratokFragment extends Fragment {

    private JaratokViewModel jaratokViewModel;
    private Context mContext;
    private RelativeLayout mRelativeLayout;
    private Database db;
    private EditText inputHonnan, inputHova;
    private List<Integer> cardList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        jaratokViewModel =
                ViewModelProviders.of(this).get(JaratokViewModel.class);
        View root = inflater.inflate(R.layout.fragment_jaratok, container, false);
        db = new Database(getActivity());
        inputHonnan = root.findViewById(R.id.inputHonnan);
        inputHova = root.findViewById(R.id.inputHova);
        cardList = new ArrayList<>();

        // https://android--code.blogspot.com/2015/12/android-how-to-create-cardview.html
        mContext = root.getContext();
        mRelativeLayout = root.findViewById(R.id.relativeLayout);
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

    public void select()
    {
        for (int i : cardList)
        {
            CardView c = mRelativeLayout.findViewById(i);
            mRelativeLayout.removeView(c);
        }
        cardList.clear();

        Cursor eredmeny = db.selectJaratok(inputHonnan.getText().toString(), inputHova.getText().toString());
        String helyekSzama = "";
        String idopont = "";
        String indulas = "";
        String indulasRovidites = "";
        String celallomas = "";
        String celallomasRovidites = "";
        String idotartam = "";
        if (eredmeny != null && eredmeny.getCount() > 0)
        {
            int index = 1;
            int id = 0;
            while (eredmeny.moveToNext())
            {
                helyekSzama = eredmeny.getString(1);
                idopont = eredmeny.getString(2);
                indulas = eredmeny.getString(3);
                indulasRovidites = eredmeny.getString(4);
                celallomas = eredmeny.getString(5);
                celallomasRovidites = eredmeny.getString(6);
                idotartam = eredmeny.getString(7);

                CardView card = new CardView(mContext);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.width = dpToPx(360);
                params.height = dpToPx(200);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                if (index == 1)
                {
                    params.addRule(RelativeLayout.BELOW, R.id.inputHova);
                }
                else
                {
                    //String below = "R.id.card" + (index - 1);
                    params.addRule(RelativeLayout.BELOW, id);
                }
                params.setMargins(0,0,0,dpToPx(20));

                card.setLayoutParams(params);
                card.setBackground(getResources().getDrawable(R.drawable.bg_jarat));
                //card.setTag("card" + index);
                //String id = "R.id.card" + index;
                card.setId(card.generateViewId());
                id = card.getId();
                cardList.add(id);

                //BUD-LHR
                TextView tvRovidites = new TextView(mContext);
                tvRovidites.setLayoutParams(params);
                tvRovidites.setText(indulasRovidites + " \u2192 " + celallomasRovidites);
                tvRovidites.setGravity(Gravity.CENTER_HORIZONTAL);
                tvRovidites.setPadding(0,dpToPx(10),0,0);
                tvRovidites.setTextSize(35);
                tvRovidites.setId(tvRovidites.generateViewId());
                int tvId = card.getId();

                //Budapest - London
                TextView tvVaros = new TextView(mContext);
                tvVaros.setLayoutParams(params);
                tvVaros.setText(indulas + " \u2192 " + celallomas);
                tvVaros.setGravity(Gravity.CENTER_HORIZONTAL);
                tvVaros.setPadding(0,dpToPx(5),0,0);
                //params.addRule(RelativeLayout.BELOW, tvId);
                // below a másik textview alá
                tvVaros.setTextSize(15);


                card.addView(tvRovidites);
                card.addView(tvVaros);
                mRelativeLayout.addView(card);

                index++;
            }
        }
        else
        {

        }
    }

    public int dpToPx(int dp)
    {
        return Math.round(dp * mContext.getResources().getDisplayMetrics().density);
    }
}