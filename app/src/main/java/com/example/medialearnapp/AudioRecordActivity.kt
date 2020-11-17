package com.example.medialearnapp

import android.content.pm.PackageManager
import android.media.AudioRecord
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.example.medialearnapp.audio.AudioRecordManager
import com.example.medialearnapp.audio.AudioTrackManager
import kotlinx.android.synthetic.main.activity_audio_record.*
import java.util.*

class AudioRecordActivity : AppCompatActivity(), View.OnClickListener {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_record)


        requestNeededPermissions()
        initListener()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestNeededPermissions() {
        val packageManager = this.packageManager
        val packInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
        val usesPermissions = packInfo.requestedPermissions
        requestPermissions(usesPermissions, 0)
    }

    private fun initRecordConfig() {
        AudioRecordManager.initConfig()
        AudioTrackManager.initConfig()
    }

    private fun initListener() {
        btn_start_record.setOnClickListener(this)
        btn_stop_record.setOnClickListener(this)
        btn_play.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_start_record -> {
                AudioRecordManager.startRecord(externalCacheDir?.path + "/test1.pcm")
            }
            R.id.btn_stop_record -> {
                AudioRecordManager.stopRecord()
            }
            R.id.btn_play -> {
                AudioTrackManager.play(externalCacheDir?.path + "/test1.pcm")
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            LogUtil.d("申请权限结果")
            initRecordConfig()
        }
    }

}