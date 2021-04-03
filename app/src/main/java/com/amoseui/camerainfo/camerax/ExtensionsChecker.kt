package com.amoseui.camerainfo.camerax

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.extensions.*

class ExtensionsChecker {

    fun getCameraInfoFromCameraX(context: Context) {
        val availability = ExtensionsManager.init(context).get()
        if (availability != ExtensionsManager.ExtensionsAvailability.LIBRARY_AVAILABLE) {
            return
        }

        // Need to check below lines with devices which have Extensions
        val imageCaptureBuilder = ImageCapture.Builder()
        val imageCaptureExtenderList = listOf(
            AutoImageCaptureExtender.create(imageCaptureBuilder),
            BeautyImageCaptureExtender.create(imageCaptureBuilder),
            BokehImageCaptureExtender.create(imageCaptureBuilder),
            HdrImageCaptureExtender.create(imageCaptureBuilder),
            NightImageCaptureExtender.create(imageCaptureBuilder)
        )

        val previewBuilder = Preview.Builder()
        val previewExtenderList = listOf(
            AutoPreviewExtender.create(previewBuilder),
            BeautyPreviewExtender.create(previewBuilder),
            BokehPreviewExtender.create(previewBuilder),
            HdrPreviewExtender.create(previewBuilder),
            NightPreviewExtender.create(previewBuilder)
        )

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()
        imageCaptureExtenderList.forEach {
            val isAvailable = it.isExtensionAvailable(cameraSelector)
            if (isAvailable) {
                println(isAvailable)
            }
        }
        previewExtenderList.forEach {
            val isAvailable = it.isExtensionAvailable(cameraSelector)
            if (isAvailable) {
                println(isAvailable)
            }
        }
    }
}