package com.test.wajdi.mypittest.DataManager;

import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.test.wajdi.mypittest.R;
import com.test.wajdi.mypittest.customs.MyImageView;
import com.test.wajdi.mypittest.interfaces.CircleCreator;

import java.util.ArrayList;

/**
 * Created by wajdi on 26/05/2018.
 */

public class PointManager {

    private static final PointManager INSTANCE = new PointManager();

    private final static int CIRCLE_DIAMETER = 100;
    private ArrayList<MyImageView> listImages = new ArrayList<>(); // stores all Circles == images

    public static PointManager getInstance() {
        return INSTANCE;
    }

    public ArrayList<MyImageView> getListImages() {
        return listImages;
    }

    /**
     * @param inflater             LayoutInflater in order to inflate the imageView
     * @param circleCreatorContext the interface context which will be firing renderImage()
     */
    public void CreateImageObject(LayoutInflater inflater, CircleCreator circleCreatorContext, boolean isFirstRend) {
        MyImageView mImage = (MyImageView) inflater.inflate(R.layout.my_image_view, null);
        mImage.setLayoutParams(new RelativeLayout.LayoutParams(CIRCLE_DIAMETER, CIRCLE_DIAMETER));
        listImages.add(mImage);
        circleCreatorContext.renderImage(mImage, isFirstRend);
    }

    /*stores the canvas width and height*/
    public float cnavasWidth;
    public float cnavasHight;

    public float getCnavasWidth() {
        return cnavasWidth;
    }

    public void setCnavasWidth(float cnavasWidth) {
        this.cnavasWidth = cnavasWidth;
    }

    public float getCnavasHight() {
        return cnavasHight;
    }

    public void setCnavasHight(float cnavasHight) {
        this.cnavasHight = cnavasHight;
    }
}
