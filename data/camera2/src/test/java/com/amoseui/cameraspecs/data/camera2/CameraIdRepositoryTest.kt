/*
 * Copyright 2024 amoseui (Amos Lim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.amoseui.cameraspecs.data.camera2

import android.app.Application
import android.content.Context
import android.hardware.Camera
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraExtensionCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.amoseui.cameraspecs.testing.shadows.ShadowCameraCharacteristicsExtended
import com.amoseui.cameraspecs.testing.shadows.ShadowCameraExtensionCharacteristics
import com.amoseui.cameraspecs.testing.shadows.ShadowCameraExtensionManager
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowCamera
import org.robolectric.shadows.ShadowCameraCharacteristics

@HiltAndroidTest
@Config(
    sdk = [Build.VERSION_CODES.TIRAMISU],
    shadows = [
        ShadowCamera::class,
        ShadowCameraCharacteristicsExtended::class,
        ShadowCameraExtensionCharacteristics::class,
        ShadowCameraExtensionManager::class,
    ],
)
@RunWith(RobolectricTestRunner::class)
class CameraIdRepositoryTest {

    @Test
    @Config(minSdk = Build.VERSION_CODES.S)
    fun refreshCameraIdsTest_minSdk_S() = runTest {
        ShadowCamera.addCameraInfo(0, Camera.CameraInfo())

        val cameraCharacteristics = ShadowCameraCharacteristicsExtended.newCameraCharacteristics()
        val shadowCameraCharacteristics = shadowOf(cameraCharacteristics)
        shadowCameraCharacteristics.set(
            CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES,
            intArrayOf(CameraMetadata.REQUEST_AVAILABLE_CAPABILITIES_LOGICAL_MULTI_CAMERA),
        )

        val cameraManager = ApplicationProvider.getApplicationContext<Application>().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val shadowCameraManager = shadowOf(cameraManager) as ShadowCameraExtensionManager
        shadowCameraManager.addCamera("0", cameraCharacteristics)
        shadowCameraManager.addCameraExtensionCharacteristics("0", ShadowCameraExtensionCharacteristics.newCameraExtensionCharacteristics())

        shadowCameraManager.addCamera("2", cameraCharacteristics)
        shadowCameraManager.addCameraExtensionCharacteristics("2", ShadowCameraExtensionCharacteristics.newCameraExtensionCharacteristics())
        shadowCameraManager.addCamera("5", cameraCharacteristics)
        shadowCameraManager.addCameraExtensionCharacteristics("5", ShadowCameraExtensionCharacteristics.newCameraExtensionCharacteristics())
        shadowCameraManager.addCamera("6", cameraCharacteristics)
        shadowCameraManager.addCameraExtensionCharacteristics("6", ShadowCameraExtensionCharacteristics.newCameraExtensionCharacteristics())

        val repository = CameraIdRepository(
            CameraIdSystemDataSource(
                ApplicationProvider.getApplicationContext(),
            ),
        )
        repository.refreshCameraIds()

        assertEquals("0", repository.cameraIdStream.first()[0].cameraId)
        assertEquals(CameraData.Type.TYPE_LOGICAL, repository.cameraIdStream.first()[0].type)
        assertEquals(CameraExtensionCharacteristics.EXTENSION_AUTOMATIC, repository.cameraIdStream.first()[0].extensionsList[0])
        assertEquals(CameraExtensionCharacteristics.EXTENSION_HDR, repository.cameraIdStream.first()[0].extensionsList[1])
        assertTrue(repository.cameraIdStream.first()[0].camera1Legacy)

        assertEquals("2", repository.cameraIdStream.first()[1].cameraId)
        assertEquals(CameraData.Type.TYPE_PHYSICAL, repository.cameraIdStream.first()[1].type)
        assertEquals(CameraExtensionCharacteristics.EXTENSION_AUTOMATIC, repository.cameraIdStream.first()[1].extensionsList[0])
        assertEquals(CameraExtensionCharacteristics.EXTENSION_HDR, repository.cameraIdStream.first()[1].extensionsList[1])
        assertFalse(repository.cameraIdStream.first()[1].camera1Legacy)

        assertEquals("5", repository.cameraIdStream.first()[2].cameraId)
        assertEquals(CameraData.Type.TYPE_PHYSICAL, repository.cameraIdStream.first()[2].type)
        assertEquals(CameraExtensionCharacteristics.EXTENSION_AUTOMATIC, repository.cameraIdStream.first()[2].extensionsList[0])
        assertEquals(CameraExtensionCharacteristics.EXTENSION_HDR, repository.cameraIdStream.first()[2].extensionsList[1])
        assertFalse(repository.cameraIdStream.first()[2].camera1Legacy)

        assertEquals("6", repository.cameraIdStream.first()[3].cameraId)
        assertEquals(CameraData.Type.TYPE_PHYSICAL, repository.cameraIdStream.first()[3].type)
        assertEquals(CameraExtensionCharacteristics.EXTENSION_AUTOMATIC, repository.cameraIdStream.first()[3].extensionsList[0])
        assertEquals(CameraExtensionCharacteristics.EXTENSION_HDR, repository.cameraIdStream.first()[3].extensionsList[1])
        assertFalse(repository.cameraIdStream.first()[3].camera1Legacy)
    }

    @Ignore("java.lang.OutOfMemoryError")
    @Test
    @Config(minSdk = Build.VERSION_CODES.P, maxSdk = Build.VERSION_CODES.R)
    fun refreshCameraIdsTest_minSdk_P_maxSdk_R() = runTest {
        val cameraCharacteristics = ShadowCameraCharacteristics.newCameraCharacteristics()
        val shadowCameraCharacteristics = shadowOf(cameraCharacteristics)
        shadowCameraCharacteristics.set(
            CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES,
            intArrayOf(),
        )

        val cameraManager = ApplicationProvider.getApplicationContext<Application>().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        shadowOf(cameraManager).addCamera("0", cameraCharacteristics)

        val repository = CameraIdRepository(
            CameraIdSystemDataSource(
                ApplicationProvider.getApplicationContext(),
            ),
        )
        repository.refreshCameraIds()

        assertEquals("0", repository.cameraIdStream.first()[0].cameraId)
        assertEquals(CameraData.Type.TYPE_NORMAL, repository.cameraIdStream.first()[0].type)
        assertTrue(repository.cameraIdStream.first()[0].extensionsList.isEmpty())
    }

    @Test
    @Config(maxSdk = Build.VERSION_CODES.O_MR1)
    fun refreshCameraIdsTest_maxSdk_OMR1() = runTest {
        val cameraManager = ApplicationProvider.getApplicationContext<Application>().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        shadowOf(cameraManager).addCamera("0", ShadowCameraCharacteristics.newCameraCharacteristics())

        val repository = CameraIdRepository(
            CameraIdSystemDataSource(
                ApplicationProvider.getApplicationContext(),
            ),
        )
        repository.refreshCameraIds()

        assertEquals("0", repository.cameraIdStream.first()[0].cameraId)
        assertEquals(CameraData.Type.TYPE_NORMAL, repository.cameraIdStream.first()[0].type)
        assertTrue(repository.cameraIdStream.first()[0].extensionsList.isEmpty())
    }
}
