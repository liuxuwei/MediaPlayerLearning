package com.example.medialearnapp.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import com.example.medialearnapp.LogUtil
import java.io.*
import java.lang.Exception

object AudioTrackManager {
    private var mAudioTrack: AudioTrack? = null

    //音频管理策略
    private const val STREAM_TYPE = AudioManager.STREAM_MUSIC

    //采样率 44.1kHz
    private const val SAMPLE_RATE_IN_HZ = 44100;

    //音频的声道数 双声道
    private const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO

    //采样格式， 数据位宽16位
    private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT

    //音频缓冲区大小
    private var mBufferSizeInBytes = 0

    //是否正在播放
    private var mIsPlaying = false

    //播放文件的路径
    private var mFilePath: String? = null

    //读取文件IO流
    private var mDataInputStream: DataInputStream? = null

    private var mPlayThread: Thread? = null


    fun initConfig() {
        if (null != mAudioTrack) {
            mAudioTrack!!.release()
        }

        mBufferSizeInBytes = AudioTrack
            .getMinBufferSize(SAMPLE_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_FORMAT)
        mAudioTrack = AudioTrack(
            STREAM_TYPE,
            SAMPLE_RATE_IN_HZ,
            CHANNEL_CONFIG,
            AUDIO_FORMAT,
            mBufferSizeInBytes,
            AudioTrack.MODE_STREAM
        )

        if (mAudioTrack == null || mAudioTrack!!.state != AudioTrack.STATE_INITIALIZED) {
            LogUtil.d("AudioTrack init Exception")
        }
    }


    @Synchronized
    fun play(filePath: String) {
        LogUtil.d("播放状态： ${mAudioTrack?.state}")
        if (mIsPlaying) {
            return
        }
        if (null != mAudioTrack && mAudioTrack!!.state == AudioTrack.STATE_INITIALIZED) {
            mAudioTrack!!.play()
        }
        this.mFilePath = filePath
        mIsPlaying = true
        mPlayThread = Thread(PlayingRunnable()).apply {
            start()
        }
    }

    private fun stopPlaying() {
        try {
            if (null != mAudioTrack) {
                mIsPlaying = false
                mAudioTrack!!.stop()

                try {
                    if (mPlayThread != null) {
                        mPlayThread!!.join()
                        mPlayThread = null
                    }
                } catch (e: InterruptedException) {
                    LogUtil.d("线程终止异常： ${e.message}")
                }
                releaseAudioTrack()
            }

        } catch (e: Exception) {
            LogUtil.d("停止AudioTrack播放异常 === ${e.message}")
        }
    }

    private fun releaseAudioTrack() {
        if (mAudioTrack?.state == AudioTrack.STATE_INITIALIZED) {
            mAudioTrack?.release()
            mAudioTrack = null
        }
    }


    class PlayingRunnable : Runnable {

        override fun run() {
            try {
                val fileInputStream = FileInputStream(File(mFilePath))
                mDataInputStream = DataInputStream(fileInputStream)
                val audioDataArr = ByteArray(mBufferSizeInBytes)
                var readLength: Int
                while (mDataInputStream!!.available() > 0) {
                    readLength = mDataInputStream!!.read(audioDataArr)
                    if (readLength > 0) {
                        mAudioTrack?.write(audioDataArr, 0, readLength)
                    }
                }
                stopPlaying()
            } catch (e: FileNotFoundException) {
                LogUtil.d("未找到要播放的音频文件： ${e.message}")
            } catch (e: IOException) {
                LogUtil.d("IOException --- ${e.message}")
            } finally {
                mDataInputStream?.close()
                mDataInputStream = null
            }
        }
    }
}