package com.test.wajdi.mypittest.page_main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.test.wajdi.mypittest.DataManager.PointManager;
import com.test.wajdi.mypittest.customs.MyImageView;
import com.test.wajdi.mypittest.customs.Point;

import java.util.ArrayList;
import java.util.Random;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.ACTION_UP;
import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_MOVE;

/**
 * Created by wajdi on 26/05/2018.
 */

public class PitView extends View {

    private static final String TAG = PitView.class.getSimpleName();
    private RelativeLayout mView;

    private int mCanvasWidth; // stores the width of the view
    private int mCanvasHeight; // stores the height of the view
    private Canvas mCanvas; // stores canvas reference for future use

    private float centerX; // stores the center x of canvas
    private float centerY; // stores the center y of canvas
    private Paint linePaint; // the paint object for drawing lines
    private final int CIRCLE_DIAMETER = 100;
    private int cr = CIRCLE_DIAMETER / 2;

    private static final Random random = new Random(); // for random coordinates

    public PitView(Context context, View view) {
        super(context);
        this.mView = (RelativeLayout) view;
        init();
    }


    /**
     * init paint object here to avoid object allocation during onDraw operation
     */
    private void init() {
        linePaint = new Paint(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCanvas = canvas;

        /*set canvas width and height as member values*/
        mCanvasWidth = canvas.getWidth();
        mCanvasHeight = canvas.getHeight();

        /*set canvas width and height as member values*/
        centerX = mCanvasWidth * 0.5f;
        centerY = mCanvasHeight * 0.5f;

        drawAxis(canvas);
        renderPathBetweenCircles();

    }

    /*------------------------------------- Inner Methods -------------------------------------- */


    private boolean isImageDragged = false; // which detected if image is moving / dragged for
    // smoother rendering, and avoid looping to detect which image is dragged
    private MyImageView mDraggedImage = null; // stores dragged Image reference

    public boolean onTouch(View v, MotionEvent event) {

        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {

            case ACTION_DOWN:
                Log.d(TAG, "Action Down");
                break;
            //ok finger is Moving, move the image
            case ACTION_MOVE:

               /*
                * because we are moving the same image, and ACTION_MOVE event will be called
                * continuously, we update the mDraggedImage Object with new values
                * and clear the flag when we drop the image (finger UP)
                */


                if (isImageDragged) {
                   /*
                    * Ok we already moving the image, don't loop through images array again,
                    * just update the centerX, centerY and x,y as shown below
                    */
                    Log.d(TAG, "------- Exist moving event occurred ");

                    //update circle x and y on the canvas
                    mDraggedImage.setX(eventX);
                    mDraggedImage.setY(eventY);

                    /*while point is dragged update center x and center y for beautiful line rendering*/
                    mDraggedImage.setCenterXY(new Point(eventX + cr, eventY + cr));


                    this.postInvalidate(); // invalidate rendering the View from a non-UI thread

                    return true;
                }

                Log.d(TAG, "------- New moving event occurred");
                Log.d(TAG, "start looping images array ");

                for (MyImageView image : PointManager.getInstance().getListImages()) //
                {
                    Log.d(TAG, "looping");

                    // check if image Touched
                    float x = image.getX();
                    float y = image.getY();

                    double centerX = x + cr;
                    double centerY = y + cr;
                    double distanceX = eventX - centerX;
                    double distanceY = eventY - centerY;

                    //Is the image Touched ?
                    if ((distanceX * distanceX) + (distanceY * distanceY) <= cr * cr) {
                        //Yes, do some stuff here :
                        isImageDragged = true;
                        mDraggedImage = image;

                        this.postInvalidate(); // invalidate rendering the View from a non-UI thread

                        /*Stores the original (x,y)*/
                        mDraggedImage.setXyPrevious(new Point(x, y));

                        Log.d(TAG, "stop looping");
                        return true; // stop looping
                    }

                } // end of looping

                break;


            case ACTION_UP:
            case ACTION_CANCEL:

                Log.d(TAG, String.format("------- Dropped  image occurred at (%f , %f)", eventX, eventY));

                if (mDraggedImage != null) {

                    checkDrop(eventX, eventY);
                    //clear Moving image Object values
                    isImageDragged = !isImageDragged;
                    mDraggedImage.setXyPrevious(null); //clear value
                    mDraggedImage = null;
                }

                break;
        } // end of switch

        return true;
    } // end of switch


    /**
     * draw axis based on canvas width and height
     *
     * @param canvas the canvas on which the background will be drawn
     */
    private void drawAxis(Canvas canvas) {
        canvas.drawLine(0, mCanvasHeight * 0.5f, mCanvasWidth, mCanvasHeight * 0.5f, linePaint);
        canvas.drawLine(mCanvasWidth * 0.5f, 0, mCanvasWidth * 0.5f, mCanvasHeight, linePaint);
    }

    /**
     * render new image on the screen and invalidate the views which in turn will
     * call the onDraw() obviously, and call the renderPathBetweenCircles() ;
     *
     * @param imageView  the image view  which will be inflated
     * @param isRandomXY random circle (x,y) center flag
     */
    public void renderImage(MyImageView imageView, boolean isRandomXY) {

        int radius = CIRCLE_DIAMETER / 2;

        if (isRandomXY) {
            // choose two random coordinates for center (x,y)

            //some padding
            int min = CIRCLE_DIAMETER * 2;
            int max = (int) (PointManager.getInstance().getCnavasWidth() - CIRCLE_DIAMETER * 2);
            float cx = random.nextInt(max - min + 1) + min;
            max = (int) (PointManager.getInstance().getCnavasHight() - CIRCLE_DIAMETER * 3);
            float cy = random.nextInt(max - min + 1) + min;

            // minus radius in order to add circle on origin Axis
            imageView.setX(cx - radius);
            imageView.setY(cy - radius);
            imageView.setCenterXY(new Point(cx, cy));

        } else {
            // minus radius in order to add circle on origin Axis
            imageView.setX(centerX - radius);
            imageView.setY(centerY - radius);
            imageView.setCenterXY(new Point(centerX, centerY));
        }
        this.mView.addView(imageView);
        this.postInvalidate();

    }

    ArrayList<Point> points = new ArrayList<>(); // stores the center (x,y) points of all circles

    /**
     * draw path between circles AND the reordering feature :)
     */
    private void renderPathBetweenCircles() {

        ArrayList<MyImageView> refCopy = PointManager.getInstance().getListImages();
        int length = refCopy.size();

        points.clear();// clear the points array in order to store and sort new values and
        // avoid filling with the same values

        /* make sure this method don't get invoked if Images array length < 2 */
        if (length < 2) {
            return;
        }

        /*add all images center (x,y) */
        for (int i = 0; i < length; i++) {
            MyImageView imageView = refCopy.get(i);
            points.add(imageView.getCenterXY());

        }

        /*sort points array in ascending order based on X value*/
        Point tempPoint;
        for (int i = 0; i < refCopy.size() - 1; i++) {
            for (int j = i + 1; j < points.size(); j++) {
                if (points.get(i).x > points.get(j).x) {
                    tempPoint = new Point(points.get(j));
                    points.set(j, points.get(i));
                    points.set(i, tempPoint);
                }
            }
        }

        //draw Lines
        for (int i = 0; i < length; i++) {
            if (i == length - 1) {
                //do nothing which mean do not draw line for the last point
                break;
            }
            float startX = points.get(i).x;
            float startY = points.get(i).y;

            float EndX = points.get(i + 1).x;
            float EndY = points.get(i + 1).y;

            mCanvas.drawLine(startX, startY, EndX, EndY, linePaint);
        }

        Log.d(TAG, "------ points array size " + points.size());


    }

    /**
     * simply checks if the circle dropped inside the canvas bounds,
     * if not just render it back where it was rendered before
     *
     * @param eventX the dropped x
     * @param eventY the dropped y
     */
    private void checkDrop(float eventX, float eventY) {
        if (eventX <= 0 || eventX >= (getRight() - CIRCLE_DIAMETER)
                || eventY <= CIRCLE_DIAMETER || eventY >= getBottom() - CIRCLE_DIAMETER) {

            mDraggedImage.setX(mDraggedImage.getXyPrevious().x);
            mDraggedImage.setY(mDraggedImage.getXyPrevious().y);
            mDraggedImage.setCenterXY(new Point(mDraggedImage.getXyPrevious().x + cr, mDraggedImage.getXyPrevious().y + cr));
            this.postInvalidate();
        }

    }

}//end of class





