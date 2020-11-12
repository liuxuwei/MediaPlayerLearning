package com.example.medialearnapp.view

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.medialearnapp.LogUtil
import com.example.medialearnapp.R
import java.lang.Exception

class CustomSurfaceView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defaultStyleAttr: Int = 0
) : SurfaceView(context, attr, defaultStyleAttr), SurfaceHolder.Callback, Runnable {
    private lateinit var mSurfaceHolder: SurfaceHolder

    private lateinit var mCanvas: Canvas

    private var mIsDrawing = false

    private lateinit var mPaint: Paint

    init {
        initView()
    }

    private fun initView() {
        mSurfaceHolder = holder
        mSurfaceHolder.addCallback(this)
        mPaint = Paint().apply {
            isAntiAlias = true
            color = Color.BLACK
            isFakeBoldText = true
            textSize = 50f
        }
        isFocusable = true
        keepScreenOn = true
        isFocusableInTouchMode = true
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        LogUtil.d("custom surface changed")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        LogUtil.d("custom surface destroy.")
        mIsDrawing = false
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        LogUtil.d("custom surface create.")
        mIsDrawing = true

        Thread(this).start()
    }

    override fun run() {
        while (mIsDrawing) {
            drawSth()
        }
    }

    private fun drawSth() {
        try {
            //获得Canvas对象
            mCanvas = mSurfaceHolder.lockCanvas()
            mCanvas.drawColor(Color.WHITE)
            mCanvas.drawText("A", 350f, 700f, mPaint)
        } catch (e: Exception) {
            LogUtil.d("drawSth exception ${e.message}")
        } finally {
            if (mCanvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas)
            }
        }
    }

}