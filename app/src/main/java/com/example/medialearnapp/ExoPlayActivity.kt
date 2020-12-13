package com.example.medialearnapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceView
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.util.MimeTypes
import kotlinx.android.synthetic.main.activity_exo_play.*

class ExoPlayActivity : AppCompatActivity() {
    private lateinit var simpleExoPlayer: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exo_play)

        initPlayer()
    }

    private fun initPlayer() {
        simpleExoPlayer =  SimpleExoPlayer.Builder(this).build()
        val mediaItem = MediaItem.Builder()
            .setUri(Uri.parse("http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8"))
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .build()

        simpleExoPlayer.setMediaItem(mediaItem)
        simpleExoPlayer.setVideoSurfaceView(SurfaceView(this))
        play_view.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
        play_view.player = simpleExoPlayer
        simpleExoPlayer.prepare()
        simpleExoPlayer.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer.stop()
    }
}