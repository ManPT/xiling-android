package com.xiling.shared.component.zuji.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by zhaoyong
 * LMPageTransformer
 */
public abstract class LMPageTransformer implements ViewPager.PageTransformer {

    /**
     * position ==  0 ：当前界面位于屏幕中心的时候
     * position ==  1 ：当前Page刚好滑出屏幕右侧
     * position == -1 ：当前Page刚好滑出屏幕左侧
     *
     * @param view
     * @param position
     */
    @Override
    public void transformPage(View view, float position) {
        if (position < -1.0f) {
            // [-Infinity,-1)
            // This page is way off-screen to the left.
            scrollInvisible(view, position);
        } else if (position <= 0.0f) {
            // [-1,0]
            // Use the default slide transition when moving to the left page
            scrollLeft(view, position);
        } else if (position <= 1.0f) {
            // (0,1]
            scrollRight(view, position);
        } else {
            // (1,+Infinity]
            // This page is way off-screen to the right.
            scrollInvisible(view, position);
        }
    }

    public abstract void scrollInvisible(View view, float position);

    public abstract void scrollLeft(View view, float position);

    public abstract void scrollRight(View view, float position);


}