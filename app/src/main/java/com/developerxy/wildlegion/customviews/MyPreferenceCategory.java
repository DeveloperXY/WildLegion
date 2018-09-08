package com.developerxy.wildlegion.customviews;

import android.content.Context;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.developerxy.wildlegion.utils.fonts.Typefaces;

public class MyPreferenceCategory extends PreferenceCategory {

    private Context mContext;

    public MyPreferenceCategory(Context context) {
        super(context);
    }

    public MyPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyPreferenceCategory(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        TextView titleView = view.findViewById(android.R.id.title);
        titleView.setTypeface(Typefaces.Companion.getFont(getContext().getAssets(), "Regular"));
    }
}