package com.leventenyiro.lightairlines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FoglalasActivity extends AppCompatActivity {

    private Context mContext;
    private RelativeLayout mRelativeLayout;
    private List<Integer> ulesLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foglalas);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        init();

        LinearLayout ll = new LinearLayout(mContext);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.height=dpToPx(35);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.setMargins(dpToPx(20), dpToPx(20), dpToPx(20), dpToPx(20));
        ll.setId(ll.generateViewId());
        int id = ll.getId();

        for (int i = 0; i < 3; i++)
        {
            TextView tv = new TextView(mContext);
            tv.setLayoutParams(params);
            tv.setBackground(getResources().getDrawable(R.drawable.ic_seatfree));
            params.width = dpToPx(35);
            params.setMargins(dpToPx(8), 0, dpToPx(8), 0);
            ll.addView(tv);
        }
        TextView tv = new TextView(mContext);
        tv.setLayoutParams(params);
        params.width = dpToPx(35);
        tv.setText("1");
        //tv.setTextSize(dpToPx(25));
        tv.setGravity(Gravity.CENTER);
        params.setMargins(dpToPx(8), 0, dpToPx(8), 0);
        ll.addView(tv);
        for (int i = 0; i < 3; i++)
        {
            tv = new TextView(mContext);
            tv.setLayoutParams(params);
            tv.setBackground(getResources().getDrawable(R.drawable.ic_seatfree));
            params.width = dpToPx(35);
            params.setMargins(dpToPx(8), 0, dpToPx(8), 0);
            ll.addView(tv);
        }
        mRelativeLayout.addView(ll);

        /*for (int i = 0; i < 19; i++)
        {
            LinearLayout ll2 = new LinearLayout(mContext);
            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params1.height = dpToPx(35);
            params1.addRule(RelativeLayout.BELOW, id);
            ll2.setGravity(View.TEXT_ALIGNMENT_CENTER);
            params1.setMargins(dpToPx(20), 0, dpToPx(20), dpToPx(20));
            ll2.setBackground(getResources().getDrawable(R.drawable.ic_seatfree));
            ll2.setId(ll2.generateViewId());
            id = ll2.getId();
            mRelativeLayout.addView(ll2);
        }*/
    }

    public void init() {
        mContext = getApplicationContext();
        mRelativeLayout = findViewById(R.id.plane);
        ulesLista = new ArrayList<>();
    }

    public int dpToPx(int dp) {
        return Math.round(dp * mContext.getResources().getDisplayMetrics().density);
    }
}
