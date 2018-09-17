package com.developerxy.wildlegion.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (parent.getChildPosition(view) == 0) {
            outRect.top = space * 2;
            outRect.bottom = space;
        }
        else if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.top = space;
            outRect.bottom = space * 2;
        }
        else {
            outRect.top = space;
            outRect.bottom = space;
        }
    }
}