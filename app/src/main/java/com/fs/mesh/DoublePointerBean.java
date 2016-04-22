package com.fs.mesh;

/**
 * Created by fangdean on 2016/4/22.
 */
public class DoublePointerBean {
    public float x0;
    public float y0;
    public float x1;
    public float y1;

    public DoublePointerBean(float x0, float y0, float x1, float y1) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }

    @Override
    public String toString() {
        return "DoublePointerBean{" +
                "x0=" + x0 +
                ", y0=" + y0 +
                ", x1=" + x1 +
                ", y1=" + y1 +
                '}';
    }
}
