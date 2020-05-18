package com.amoseui.camerainfo.camerax

import android.annotation.SuppressLint
import android.content.Context
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraX
import androidx.camera.extensions.ExtensionsManager

class ExtensionsChecker {

    @SuppressLint("RestrictedApi")
    fun getCameraInfoFromCameraX(context: Context): Map<Int, MutableSet<String>> {
        val availability = ExtensionsManager.init().get()
        if (availability != ExtensionsManager.ExtensionsAvailability.LIBRARY_AVAILABLE) {
            return emptyMap()
        }

        CameraX.initialize(context, Camera2Config.defaultConfig())
        val extensionsData = mapOf(
            CameraSelector.LENS_FACING_BACK to mutableSetOf<String>(),
            CameraSelector.LENS_FACING_FRONT to mutableSetOf()
        )
        extensionsData.forEach {
            ExtensionsManager.EffectMode.values().forEach { effectMode ->
                if (ExtensionsManager.isExtensionAvailable(effectMode, it.key)) {
                    it.value.add(effectMode.name)
                }
            }
        }
        CameraX.shutdown()

        return extensionsData
    }
}