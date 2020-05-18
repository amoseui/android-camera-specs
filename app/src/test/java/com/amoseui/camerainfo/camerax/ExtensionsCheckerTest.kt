package com.amoseui.camerainfo.camerax

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Config.OLDEST_SDK])
class ExtensionsCheckerTest {

    @Mock
    private lateinit var mockContext: Context

    @Test
    fun testGetCameraInfoFromCameraX() {
        val expectedData = mapOf(
            CameraSelector.LENS_FACING_BACK to mutableSetOf<String>(),
            CameraSelector.LENS_FACING_FRONT to mutableSetOf()
        )
//        `when`(extensionsManager.init())
//            .thenReturn(ExtensionsManager.ExtensionsAvailability.LIBRARY_AVAILABLE)

        val context = ApplicationProvider.getApplicationContext<Context>()
        assertEquals(emptyMap<Int, MutableSet<String>>(), ExtensionsChecker().getCameraInfoFromCameraX(context))
    }
}