package com.gkpoter.canvastest.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView

/**
 * Created by BaiGuoQing on 2019/9/22.
 */

@SuppressLint("AppCompatCustomView")
class GridTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    private var mPercentWidth: FloatArray = floatArrayOf()
    private var mPercentHeight: FloatArray = floatArrayOf()
    private var mGridNumOfWidth: Int = 0
    private var mGridNumofHeiht: Int = 0

    private var mText: Array<String> = arrayOf()

    private var isRoundBackground = false
    private var mRadius: FloatArray = floatArrayOf()
    private val mRadiusDefault = 30f
    private var mRoundBackgroundColor = -0x1000000

    fun setGridNumOfWidth(gridNumOfWidth: Int, percent: FloatArray) {
        if (gridNumOfWidth > percent.size) {
            throw RuntimeException("gridNumOfWidth can not more then the percent[] length")
        }
        mGridNumOfWidth = gridNumOfWidth
        mPercentWidth = percent
    }

    fun setGridNumofHeiht(gridNumofHeiht: Int, percent: FloatArray) {
        if (gridNumofHeiht > percent.size) {
            throw RuntimeException("gridNumofHeiht can not more then the percent[] length")
        }
        mGridNumofHeiht = gridNumofHeiht
        mPercentHeight = percent
    }

    /**
     * after [.setGridNumOfWidth]
     * after [.setGridNumofHeiht]
     *
     * @param text content array
     */
    fun setText(texts: Array<String>) {
        if (mGridNumOfWidth == 0 || mGridNumofHeiht == 0) {
            throw RuntimeException("please after setGridNumofHeiht() and setGridNumOfWidth()")
        }
        if (texts.size < mGridNumOfWidth * mGridNumofHeiht) {
            throw RuntimeException("text array length must more then the GridNum")
        }
        mText = texts
        invalidate()
    }

    /**
     * @param roundBackGround background is show or not
     */
    fun setRoundBackground(roundBackGround: Boolean) {
        isRoundBackground = roundBackGround
        invalidate()
    }

    /**
     * @param roundBackgroundColor round background color
     */
    fun setRoundBackgroundColor(roundBackgroundColor: Int) {
        mRoundBackgroundColor = roundBackgroundColor
    }

    /**
     * @param xradius background rect x radius
     * @param yradius background rect y radius
     */
    fun setRadius(xradius: Float, yradius: Float) {
        mRadius = FloatArray(2)
        mRadius[0] = xradius
        mRadius[1] = yradius
        invalidate()
    }

    /**
     * canvas
     */
    override fun onDraw(canvas: Canvas) {

        val vLeft = 0
        val vTop = 0
        val vRight = right - left
        val vBottom = bottom - top
        val vWidth = width - paddingLeft - paddingRight
        val vHeight = height - paddingTop - paddingBottom

        if (isRoundBackground) {
            @SuppressLint("DrawAllocation") val paint = Paint()
            paint.color = mRoundBackgroundColor
            paint.style = Paint.Style.FILL
            if (mRadius.isNotEmpty()) {
                canvas.drawRoundRect(
                    vLeft.toFloat(), vTop.toFloat(), vRight.toFloat(), vBottom.toFloat(),
                    mRadius[0], mRadius[1], paint
                )
            } else {
                canvas.drawRoundRect(
                    vLeft.toFloat(), vTop.toFloat(), vRight.toFloat(), vBottom.toFloat(),
                    mRadiusDefault, mRadiusDefault, paint
                )
            }
        }
        if (mGridNumOfWidth != 0 && mPercentWidth.isNotEmpty()) {
            val paint = paint
            var index = 0
            var top = vTop + paddingTop
            for (i in 0 until mGridNumofHeiht) {
                val bottom = (top + vHeight * mPercentHeight[i]).toInt()
                var left = vLeft + paddingLeft
                for (j in 0 until mGridNumOfWidth) {
                    val right = (left + vWidth * mPercentWidth[j]).toInt()
                    drawText(canvas, mText[index++], left, top, right, bottom, paint)
                    left = right
                }
                top = bottom
            }
        }
    }

    @SuppressLint("RtlHardcoded")
    private fun drawText(
        canvas: Canvas, text: String,
        startx: Int, starty: Int, endx: Int, endy: Int, paint: Paint
    ) {
        @SuppressLint("DrawAllocation") val path = Path()
        val fontMetrics = paint.fontMetrics

        //默认左上
        var baseline = starty - fontMetrics.top

        when (gravity) {
            Gravity.CENTER -> {
                val distance = (fontMetrics.bottom - fontMetrics.top) / 2f - fontMetrics.bottom
                baseline = (starty + endy) / 2f + distance
                paint.textAlign = Paint.Align.CENTER
            }
            Gravity.TOP + Gravity.CENTER_HORIZONTAL -> {
                //上水平
                paint.textAlign = Paint.Align.CENTER
            }
            Gravity.BOTTOM + Gravity.CENTER_HORIZONTAL -> {
                //下水平
                paint.textAlign = Paint.Align.CENTER
                baseline = endy - fontMetrics.descent
            }
            Gravity.LEFT + Gravity.CENTER_VERTICAL, Gravity.START + Gravity.CENTER_VERTICAL -> {
                //左垂直
                val distance = (fontMetrics.bottom - fontMetrics.top) / 2f - fontMetrics.bottom
                baseline = (starty + endy) / 2f + distance
            }
            Gravity.RIGHT + Gravity.CENTER_VERTICAL, Gravity.END + Gravity.CENTER_VERTICAL -> {
                //右垂直
                paint.textAlign = Paint.Align.RIGHT
                val distance = (fontMetrics.bottom - fontMetrics.top) / 2f - fontMetrics.bottom
                baseline = (starty + endy) / 2f + distance
            }
            Gravity.LEFT + Gravity.BOTTOM, Gravity.START + Gravity.BOTTOM -> {
                //左下
                baseline = endy - fontMetrics.descent
            }
            Gravity.RIGHT + Gravity.TOP, Gravity.END + Gravity.TOP -> {
                //右上
                paint.textAlign = Paint.Align.RIGHT
            }
            Gravity.RIGHT + Gravity.BOTTOM, Gravity.END + Gravity.BOTTOM -> {
                //右下
                paint.textAlign = Paint.Align.RIGHT
                baseline = endy - fontMetrics.descent
            }
            else -> {
                //默认左上
                paint.textAlign = Paint.Align.LEFT
                baseline = starty - fontMetrics.top
            }
        }

        path.moveTo(startx.toFloat(), baseline)
        path.lineTo(endx.toFloat(), baseline)
        canvas.drawTextOnPath(text, path, 0f, 0f, paint)
    }
}
