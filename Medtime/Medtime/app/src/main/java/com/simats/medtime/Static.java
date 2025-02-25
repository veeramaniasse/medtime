package com.simats.medtime;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Static {

    public static void glide(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).placeholder(R.drawable.calendar).error(R.drawable.edt).into(imageView);
    }
}
