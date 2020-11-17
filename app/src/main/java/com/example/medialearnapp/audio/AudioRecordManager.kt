package com.example.medialearnapp.audio

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import com.example.medialearnapp.LogUtil
import java.io.*
import java.lang.Exception

object AudioRecordManager {

    //音频采集的输入源 --- 麦克风
    private const val AUDIO_SOURCE = MediaRecorder.AudioSource.MIC

    //采样率
    private const val SAMPLE_RATE_IN_HZ = 44100

    //声道： 双声道的立体声
    private const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO

    //音频采集格式， 数据位宽16位
    private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT

    private var mAudioRecord: AudioRecord? = null

    //音频缓冲区大小
    private var mBufferSizeInBytes = 0

    //是否正在录音
    private var mIsRecording: Boolean = false

    private var mFileOutputStream: FileOutputStream? = null

    private var mOutputFilePath: String? = null

    private var mRecordThread: Thread? = null

    fun initConfig() {
        try {
            mBufferSizeInBytes = AudioRecord
                .getMinBufferSize(SAMPLE_RATE_IN_HZ, CHANNEL_CONFIG, AUDIO_FORMAT)
            mAudioRecord = AudioRecord(
                AUDIO_SOURCE,
                SAMPLE_RATE_IN_HZ,
                CHANNEL_CONFIG,
                AUDIO_FORMAT,
                mBufferSizeInBytes
            )
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            LogUtil.d("采样率44.1kHz的AudioRecord 初始化异常 + ${e.message}")
        }

        if (mAudioRecord?.state != AudioRecord.STATE_INITIALIZED) {
            LogUtil.d("AudioRecord 初始化异常")
        }
    }

    /**
     * 开始录音
     */
    fun startRecord(filePath: String) {
        if (mAudioRecord?.state == AudioRecord.STATE_INITIALIZED) {
            mAudioRecord?.startRecording()
        }

        mIsRecording = true
        mRecordThread = Thread(RecordRunnable())
        try {
            this.mOutputFilePath = filePath
            LogUtil.d("path ==== $mOutputFilePath")
            val file = File(mOutputFilePath)
            if (!file.exists()) {
                LogUtil.d("mkdir === ")
                file.createNewFile()
                mRecordThread!!.start()
            }
        } catch (e: Exception) {
            LogUtil.d("start record exception ${e.message}")
        }
    }

    fun stopRecord() {
        try {
            mIsRecording = false
            try {
                mRecordThread?.join()
                mRecordThread = null
                releaseAudioRecord()
            } catch (e: Exception) {
                LogUtil.d("release record exception ${e.message}")
            }
        } catch (e: Exception) {
            LogUtil.d("stop record exception ${e.message}")
        }
    }

    private fun releaseAudioRecord() {
        if (mAudioRecord?.state == AudioRecord.STATE_INITIALIZED) {
            mAudioRecord?.stop()
            mAudioRecord?.release()
            mAudioRecord = null
        }
    }

    class RecordRunnable : Runnable {
        override fun run() {
            try {
                mFileOutputStream = FileOutputStream(mOutputFilePath)
                val audioDataArr = ByteArray(mBufferSizeInBytes)
                while (mIsRecording) {
                    val audioDataSize = getAudioRecordBufferSize(mBufferSizeInBytes, audioDataArr)
                    if (audioDataSize > 0) {
                        LogUtil.d("writing pcm data...")
                        mFileOutputStream?.write(audioDataArr)
                    } else {
                        mIsRecording = false
                    }
                }
            } catch (e: FileNotFoundException) {
                LogUtil.d("file not exist ${e.message}")
            } catch (e: IOException) {
                LogUtil.d("ioException ${e.message}")
            } finally {
                if (mFileOutputStream != null) {
                    mFileOutputStream!!.close()
                    mFileOutputStream = null
                }
            }
        }

    }

    private fun getAudioRecordBufferSize(bufferSizeInBytes: Int, audioDataArr: ByteArray): Int {
        if (mAudioRecord != null) {
            return mAudioRecord!!.read(audioDataArr, 0, bufferSizeInBytes)
        }
        return 0
    }
}