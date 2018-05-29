package com.test.wajdi.mypittest.customs;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by wajdi on 26/05/2018.
 * <p>
 * the image view class which hold  (x,y) position
 */

public class MyImageView extends ImageView {
    private Point centerXY;
    private Point xyPrevious; // stores the xy center before get dragged

    public Point getXyPrevious() {
        return xyPrevious;
    }

    public void setXyPrevious(Point xyPrevious) {
        this.xyPrevious = xyPrevious;
    }

    public MyImageView(Context context, Point centerXY) {
        super(context);
        this.centerXY = centerXY;
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Point getCenterXY() {
        return centerXY;
    }

    public void setCenterXY(Point xy) {
        this.centerXY = xy;
    }

}
