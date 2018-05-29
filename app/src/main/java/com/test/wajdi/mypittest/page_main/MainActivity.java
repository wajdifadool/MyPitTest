package com.test.wajdi.mypittest.page_main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.test.wajdi.mypittest.customs.MyImageView;
import com.test.wajdi.mypittest.DataManager.PointManager;
import com.test.wajdi.mypittest.interfaces.CircleCreator;
import com.test.wajdi.mypittest.R;

public class MainActivity
        extends AppCompatActivity
        implements CircleCreator,
        View.OnClickListener,
        View.OnTouchListener,
        ViewTreeObserver.OnGlobalLayoutListener {

    private RelativeLayout parentLayout;
    private PitView pitView;
    private LayoutInflater layoutInflater;
    private int count = 5; // first 5 circles to render
    private ViewTreeObserver vto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        parentLayout = findViewById(R.id.relative_layout);
        pitView = new PitView(this, parentLayout);
        parentLayout.addView(pitView);

        findViewById(R.id.button_create_point).setOnClickListener(this);
        pitView.setOnTouchListener(this);

       /* attach listener for the layout that hold the canvas  which will be called when
        * state or the visibility of the view changes
        */

        vto = parentLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void createImage() {
        PointManager.getInstance().CreateImageObject(layoutInflater, this, count > 0);
    }

    @Override
    public void renderImage(MyImageView imageView, boolean isFirstRend) {
        pitView.renderImage(imageView, isFirstRend);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_create_point:
                this.createImage();
                break;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return pitView.onTouch(v, event); // call PitView onTouch function
    }

    @Override
    public void onGlobalLayout() {
       /* when this callback get fired store the canvas with and height as fields
        *in PointManager.java which will be used when render the first 5 circles
        */

        parentLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        int width = parentLayout.getMeasuredWidth();
        int height = parentLayout.getMeasuredHeight();

        PointManager.getInstance().setCnavasHight(height);
        PointManager.getInstance().setCnavasWidth(width);

        while (count > 0) {
            createImage();
            count--;
        }
    }


}
