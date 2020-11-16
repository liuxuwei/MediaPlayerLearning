package com.example.medialearnapp.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import com.example.medialearnapp.LogUtil
import com.example.medialearnapp.R
import kotlin.math.min

class CircleImageView @JvmOverloads constructor(
    private val mContext: Context,
    private val mAttrs: AttributeSet? = null,
    private val mDefaultStyleAttr: Int = 0
) : ImageView(mContext, mAttrs, mDefaultStyleAttr) {

    companion object {
        const val DEFAULT_BORDER_WIDTH = 1f
        const val DEFAULT_BORDER_COLOR = Color.BLUE
    }

    private var mBorderColor = DEFAULT_BORDER_COLOR
    private var mBorderWidth = DEFAULT_BORDER_WIDTH
    private val mBitmapPaint = Paint().apply {
        isAntiAlias = true
        isDither = true
        isFilterBitmap = true
    }

    private lateinit var mBitmapCanvas: Canvas

    private lateinit var mBorderPaint: Paint

    private lateinit var mBorderRect: RectF

    init {
        initAttrs()
        initTools()
    }

    private fun initTools() {
        mBorderPaint = Paint().apply {
            style = Paint.Style.STROKE
            isAntiAlias = true
            color = mBorderColor
            strokeWidth = mBorderWidth
        }

        mBorderRect = RectF()
    }

    private fun initAttrs() {
        val typedArray = mContext
            .obtainStyledAttributes(
                mAttrs,
                R.styleable.CircleImageView,
                mDefaultStyleAttr, 0
            )

        mBorderColor = typedArray.getColor(
            R.styleable.CircleImageView_circle_border_color,
            DEFAULT_BORDER_COLOR
        )

        mBorderWidth = typedArray.getDimensionPixelSize(
            R.styleable.CircleImageView_circle_border_width,
            DEFAULT_BORDER_WIDTH.toInt()
        ).toFloat()


        typedArray.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        LogUtil.d("now size is width = $measuredWidth and height = $measuredHeight ")
        mBorderRect.set(calculateBound())
        val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        mBitmapCanvas = Canvas(bitmap)
        invalidate()
    }

    private fun calculateBound(): RectF {
        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom

        val sideLength = min(availableWidth, availableHeight)

        val left = paddingLeft + (availableWidth - sideLength) / 2
        val top = paddingTop + (availableHeight - sideLength) / 2

        return RectF(
            left.toFloat(), top.toFloat(),
            (left + sideLength).toFloat(),
            (top + sideLength).toFloat()
        )
    }


    override fun onDraw(canvas: Canvas?) {
        LogUtil.d("onDraw ====")
        super.onDraw(canvas)

        drawable.draw(mBitmapCanvas)

    }

}