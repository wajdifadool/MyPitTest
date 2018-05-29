package com.test.wajdi.mypittest.customs;

/**
 * Created by wajdi on 28/05/2018.
 */

public class Point {
    public Float x;
    public Float y;

    public Point(Point src) {
        this.x = src.x;
        this.y = src.y;

    }

    public Point(Float x, Float y) {
        this.x = x;
        this.y = y;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getX() {
        return x;
    }

    public Float getY() {
        return y;
    }
}
