package com.gkpoter.canvastest.java;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by BaiGuoQing on 2019/9/22.
 */

@SuppressLint("AppCompatCustomView")
public class GridTextView extends TextView {

    private float[] mPercentWidth;
    private float[] mPercentHeight;
    private int mGridNumOfWidth;
    private int mGridNumofHeiht;

    private String[] mText;

    private boolean isRoundBackground = false;
    private float[] mRadius;
    private final float mRadiusDefault = 30f;
    private int mRoundBackgroundColor = 0xff000000;

    public GridTextView(Context context) {
        this(context, null);
    }

    public GridTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setGridNumOfWidth(int gridNumOfWidth, float[] percent) {
        if (gridNumOfWidth > percent.length) {
            throw new RuntimeException("gridNumOfWidth can not more then the percent[] length");
        }
        this.mGridNumOfWidth = gridNumOfWidth;
        mPercentWidth = percent;
    }

    public void setGridNumofHeiht(int gridNumofHeiht, float[] percent) {
        if (gridNumofHeiht > percent.length) {
            throw new RuntimeException("gridNumofHeiht can not more then the percent[] length");
        }
        this.mGridNumofHeiht = gridNumofHeiht;
        mPercentHeight = percent;
    }

    /**
     * after {@link #setGridNumOfWidth(int, float[])}
     * after {@link #setGridNumofHeiht(int, float[])}
     *
     * @param text content array
     */
    public void setText(String[] text) {
        if (mGridNumOfWidth == 0 || mGridNumofHeiht == 0) {
            throw new RuntimeException("please after setGridNumofHeiht() and setGridNumOfWidth()");
        }
        if (text.length < mGridNumOfWidth * mGridNumofHeiht) {
            throw new RuntimeException("text array length must more then the GridNum");
        }
        mText = text;
        invalidate();
    }

    /**
     * @param roundBackGround background is show or not
     */
    public void setRoundBackground(boolean roundBackGround) {
        isRoundBackground = roundBackGround;
        invalidate();
    }

    /**
     * @param roundBackgroundColor round background color
     */
    public void setRoundBackgroundColor(int roundBackgroundColor) {
        mRoundBackgroundColor = roundBackgroundColor;
        invalidate();
    }

    /**
     * @param xradius background rect x radius
     * @param yradius background rect y radius
     */
    public void setRadius(float xradius, float yradius) {
        mRadius = new float[2];
        mRadius[0] = xradius;
        mRadius[1] = yradius;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int vLeft = 0;
        int vTop = 0;
        int vRight = getRight() - getLeft();
        int vBottom = getBottom() - getTop();
        int vWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int vHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        if (isRoundBackground) {
            @SuppressLint("DrawAllocation") Paint paint = new Paint();
            paint.setColor(mRoundBackgroundColor);
            paint.setStyle(Paint.Style.FILL);
            if (mRadius != null) {
                canvas.drawRoundRect(vLeft, vTop, vRight, vBottom,
                        mRadius[0], mRadius[1], paint);
            } else {
                canvas.drawRoundRect(vLeft, vTop, vRight, vBottom,
                        mRadiusDefault, mRadiusDefault, paint);
            }
        }
        if (mGridNumOfWidth != 0 && mPercentWidth != null) {
            TextPaint paint = getPaint();
            int index = 0;
            int top = vTop + getPaddingTop();
            for (int i = 0; i < mGridNumofHeiht; i++) {
                int bottom = (int) (top + vHeight * mPercentHeight[i]);
                int left = vLeft + getPaddingLeft();
                for (int j = 0; j < mGridNumOfWidth; j++) {
                    int right = (int) (left + vWidth * mPercentWidth[j]);
                    drawText(canvas, mText[index++], left, top, right, bottom, paint);
                    left = right;
                }
                top = bottom;
            }
        }
    }

    @SuppressLint("RtlHardcoded")
    private void drawText(Canvas canvas, String text,
                          int startx, int starty, int endx, int endy, Paint paint) {
        @SuppressLint("DrawAllocation") Path path = new Path();
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();

        //默认左上
        float baseline = starty - fontMetrics.top;

        switch (getGravity()) {
            case Gravity.CENTER: {
                float distance = (fontMetrics.bottom - fontMetrics.top) / 2f - fontMetrics.bottom;
                baseline = (starty + endy) / 2f + distance;
                paint.setTextAlign(Paint.Align.CENTER);
                break;
            }
            case Gravity.TOP + Gravity.CENTER_HORIZONTAL: {
                //上水平
                paint.setTextAlign(Paint.Align.CENTER);
                break;
            }
            case Gravity.BOTTOM + Gravity.CENTER_HORIZONTAL: {
                //下水平
                paint.setTextAlign(Paint.Align.CENTER);
                baseline = endy - fontMetrics.descent;
                break;
            }
            case Gravity.LEFT + Gravity.CENTER_VERTICAL:
            case Gravity.START + Gravity.CENTER_VERTICAL: {
                //左垂直
                float distance = (fontMetrics.bottom - fontMetrics.top) / 2f - fontMetrics.bottom;
                baseline = (starty + endy) / 2f + distance;
                break;
            }
            case Gravity.RIGHT + Gravity.CENTER_VERTICAL:
            case Gravity.END + Gravity.CENTER_VERTICAL: {
                //右垂直
                paint.setTextAlign(Paint.Align.RIGHT);
                float distance = (fontMetrics.bottom - fontMetrics.top) / 2f - fontMetrics.bottom;
                baseline = (starty + endy) / 2f + distance;
                break;
            }
            case Gravity.LEFT + Gravity.BOTTOM:
            case Gravity.START + Gravity.BOTTOM: {
                //左下
                baseline = endy - fontMetrics.descent;
                break;
            }
            case Gravity.RIGHT + Gravity.TOP:
            case Gravity.END + Gravity.TOP: {
                //右上
                paint.setTextAlign(Paint.Align.RIGHT);
                break;
            }
            case Gravity.RIGHT + Gravity.BOTTOM:
            case Gravity.END + Gravity.BOTTOM: {
                //右下
                paint.setTextAlign(Paint.Align.RIGHT);
                baseline = endy - fontMetrics.descent;
                break;
            }
            default:
                //默认左上
                paint.setTextAlign(Paint.Align.LEFT);
                baseline = starty - fontMetrics.top;
                break;
        }

        path.moveTo(startx, baseline);
        path.lineTo(endx, baseline);
        canvas.drawTextOnPath(text, path, 0, 0, paint);
    }
}
