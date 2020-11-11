package com.example.medialearnapp

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.example.medialearnapp.PlayConstants.Companion.ACTION_INIT
import com.example.medialearnapp.PlayConstants.Companion.ACTION_PLAY

class PlayService: Service(), MediaPlayer.OnErrorListener {

    private var mMediaPlayer: MediaPlayer? = null

    override fun onCreate() {
        super.onCreate()
        LogUtil.d("service onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LogUtil.d("onStartCommand")
        when (intent?.action) {
            ACTION_INIT -> {
                LogUtil.d("service init MediaPlayer")
                mMediaPlayer = MediaPlayer.create(applicationContext, R.raw.lianqu)
                mMediaPlayer?.apply {
                    setOnErrorListener(this@PlayService)
                }
            }
            ACTION_PLAY -> {
                LogUtil.d("service start play music")
                mMediaPlayer?.start()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onDestroy() {
        super.onDestroy()
        LogUtil.d("play service onDestroy.")
        mMediaPlayer?.release()
        mMediaPlayer = null
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        LogUtil.d("error what is $what  extra is $extra")
        return false
    }
}