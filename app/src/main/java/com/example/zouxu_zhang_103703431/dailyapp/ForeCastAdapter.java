package com.example.zouxu_zhang_103703431.dailyapp;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by Administrator on 2017/4/13.
 */

public class ForeCastAdapter extends ArrayAdapter<String> {
    public ForeCastAdapter(Context context, String[] date_text) {
        super(context, R.layout.textview, date_text);

    }

}