package com.example.medialearnapp.view

import android.content.Context
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import com.example.medialearnapp.LogUtil

class MediaSurfaceView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultStyleAttr: Int = 0
) : SurfaceView(context, attributeSet, defaultStyleAttr),
    SurfaceHolder.Callback,
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnCompletionListener,
    View.OnClickListener {
    companion object {
        const val VIDEO_URL = "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4"
    }

    private var mCurrentPosition: Int = 0
    private var mMediaPlayer: MediaPlayer? = null
    private val mSurfaceHolder: SurfaceHolder by lazy {
        holder
    }

    init {
        mSurfaceHolder.addCallback(this)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
            mCurrentPosition = mMediaPlayer!!.currentPosition
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (null == mMediaPlayer) {
            mMediaPlayer = MediaPlayer().apply {
                keepScreenOn = true
                setDisplay(holder)
                setOnPreparedListener(this@MediaSurfaceView)
                setOnCompletionListener(this@MediaSurfaceView)
                setOnClickListener(this@MediaSurfaceView)
            }
        }
        mMediaPlayer?.let {
            LogUtil.d("setDataSource ===== ")
            it.setDataSource(VIDEO_URL)
            it.prepareAsync()
        }
    }

    override fun onPrepared(mp: MediaPlayer?) {
        LogUtil.d("MediaPlayer prepare successful.")
        mp?.start()
        mp?.seekTo(mCurrentPosition)
    }

    override fun onClick(v: View?) {
        mMediaPlayer?.let {
            if (it.isPlaying) {
                LogUtil.d("mediaPlayer playing -----> pause")
                mCurrentPosition = it.currentPosition
                it.pause()
                return
            }
            LogUtil.d("mediaPlayer pause -----》 play")
            it.start()
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        LogUtil.d("mediaPlayer play complete callback.")
    }
}