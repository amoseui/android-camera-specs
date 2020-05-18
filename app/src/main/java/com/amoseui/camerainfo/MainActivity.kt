package com.amoseui.camerainfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amoseui.camerainfo.camerax.ExtensionsChecker

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ExtensionsChecker().getCameraInfoFromCameraX(baseContext)
    }
}
