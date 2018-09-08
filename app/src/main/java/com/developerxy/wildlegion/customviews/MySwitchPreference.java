package com.developerxy.wildlegion.customviews;

import android.content.Context;
import android.preference.PreferenceCategory;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.developerxy.wildlegion.R;
import com.developerxy.wildlegion.utils.fonts.Typefaces;

public class MySwitchPreference extends SwitchPreference {

    private Context mContext;

    public MySwitchPreference(Context context) {
        super(context);
    }

    public MySwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySwitchPreference(Context context, AttributeSet attrs,
                              int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        TextView title = view.findViewById(android.R.id.title);
        TextView summary = view.findViewById(android.R.id.summary);

        title.setTextColor(getContext().getResources().getColor(R.color.field_text_color));
        summary.setTextColor(getContext().getResources().getColor(R.color.field_text_color));

        title.setTypeface(Typefaces.Companion.getFont(getContext().getAssets(), "Bold"));
        summary.setTypeface(Typefaces.Companion.getFont(getContext().getAssets(), "Regular"));
    }
}