package com.dew.easeeye

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.SeekBar
import android.widget.Toast
import com.dew.easeeye.Utility.FLOAT_WINDOW_REQUEST_CODE
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    private var floatWindowPermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        filterLevelSeekBar.setOnSeekBarChangeListener(this)

        checkPermission()
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (Settings.canDrawOverlays(this)) {
                floatWindowPermission = true
                startBlueLightFilterService()
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                intent.data = Uri.parse("package:$packageName")
                startActivityForResult(intent, FLOAT_WINDOW_REQUEST_CODE)
            }
        } else {
            floatWindowPermission = true
            startBlueLightFilterService()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FLOAT_WINDOW_REQUEST_CODE -> {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (Settings.canDrawOverlays(this)) {
                        floatWindowPermission = true
                        startBlueLightFilterService()
                        Toast.makeText(this@MainActivity, "Permission Granted", Toast.LENGTH_LONG).show()
                    } else {
                        floatWindowPermission = false
                        Toast.makeText(this@MainActivity, "Permission Denied", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun startBlueLightFilterService() {
        val intent = Intent(this, FilteringService::class.java)
        intent.action = Utility.START_FOREGROUND_ACTION
        startService(intent)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (floatWindowPermission) {
            val intent = Intent(this, FilteringService::class.java)
            intent.putExtra("level", progress)
            startService(intent)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

}
