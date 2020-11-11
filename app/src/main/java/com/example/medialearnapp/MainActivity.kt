package com.example.medialearnapp

import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mIntent: Intent
    private lateinit var mMediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        initMediaPlayer()
        initListener()
    }

    private fun initListener() {
        btn_play.setOnClickListener(this)
        btn_init.setOnClickListener(this)
    }

    private fun initMediaPlayer() {
        val musicUrl = "http://sc1.111ttt.cn/2017/1/05/09/298092035545.mp3"
        try {
            mMediaPlayer = MediaPlayer().apply {
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setDataSource(applicationContext, Uri.parse(musicUrl))
                setOnErrorListener { _, _, _ ->
                    Log.d(TAG, "onError:")
                    false
                }
                setOnPreparedListener {
                    Log.d(TAG, "initMediaPlayer:  onPrepare successful")
                }
                prepare()
            }
        } catch (e: Exception) {
            Log.d(TAG, "exception initMediaPlayer: ${e.message}")
        }

    }

    override fun onClick(v: View?) {
        mIntent = Intent(this, PlayService::class.java)
        if (v?.id == R.id.btn_play) {
            LogUtil.d("onClick btn ")
            mIntent.action = PlayConstants.ACTION_PLAY
        }else if (v?.id == R.id.btn_init) {
            mIntent.action = PlayConstants.ACTION_INIT
        }
        mIntent.setPackage("com.example.medialearnapp")
        startService(mIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(mIntent)
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}