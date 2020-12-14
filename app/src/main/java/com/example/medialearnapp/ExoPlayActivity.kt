package com.example.medialearnapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceView
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
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
        simpleExoPlayer =  SimpleExoPlayer.Builder(this).build().apply {
            addListener(object: Player.EventListener{
                override fun onPlaybackStateChanged(state: Int) {
                    super.onPlaybackStateChanged(state)
                    LogUtil.d("onPlaybackStateChanged now state is $state")
                    //STATE_IDLE  initial state, does not have any media to play
                    //STATE_BUFFERING  player is not able to immediately play from current position
                                     //this mostly happens because more data needs to be loaded;
                    //STATE_READY  player is able to immediately play from current position;

                }

                override fun onPlayerError(error: ExoPlaybackException) {
                    super.onPlayerError(error)
                    LogUtil.d("onPlayerError msg is ${error.message}")
                }

                override fun onPositionDiscontinuity(reason: Int) {
                    super.onPositionDiscontinuity(reason)
                    LogUtil.d("onPositionDiscontinuity reason is $reason")

                }
            })
        }
        val mediaItem = MediaItem.Builder()
            .setUri(Uri.parse("http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8"))
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .build()

        simpleExoPlayer.setMediaItem(mediaItem)
        play_view.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
        play_view.player = simpleExoPlayer
        simpleExoPlayer.prepare()
        simpleExoPlayer.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer.stop()
        simpleExoPlayer.release()
    }
}