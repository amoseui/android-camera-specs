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
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowCameraCharacteristics

@HiltAndroidTest
@Config(
    sdk = [Build.VERSION_CODES.TIRAMISU],
    shadows = [
        ShadowCameraCharacteristicsExtended::class,
        ShadowCameraExtensionCharacteristics::class,
        ShadowCameraExtensionManager::class,
    ],
)
@RunWith(RobolectricTestRunner::class)
class CameraIdsRepositoryTest {

    @Test
    @Config(minSdk = Build.VERSION_CODES.S)
    fun refreshCameraIdsTest_minSdk_S() = runTest {
        val cameraManager = ApplicationProvider.getApplicationContext<Application>().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val shadowCameraManager = shadowOf(cameraManager) as ShadowCameraExtensionManager

        val cameraCharacteristics = ShadowCameraCharacteristicsExtended.newCameraCharacteristics()
        val shadowCameraCharacteristics = shadowOf(cameraCharacteristics)
        shadowCameraCharacteristics.set(
            CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES,
            intArrayOf(CameraMetadata.REQUEST_AVAILABLE_CAPABILITIES_LOGICAL_MULTI_CAMERA),
        )

        shadowCameraManager.addCamera("0", cameraCharacteristics)
        shadowCameraManager.addCameraExtensionCharacteristics("0", ShadowCameraExtensionCharacteristics.newCameraExtensionCharacteristics())

        val repository = CameraIdsRepository(
            CameraIdsSystemDataSource(
                ApplicationProvider.getApplicationContext(),
            ),
        )
        repository.refreshCameraIds()

        assertEquals("0", repository.cameraIdsStream.first()[0].cameraId)
        assertEquals(CameraData.Type.TYPE_LOGICAL, repository.cameraIdsStream.first()[0].type)

        assertEquals("2", repository.cameraIdsStream.first()[1].cameraId)
        assertEquals(CameraData.Type.TYPE_PHYSICAL, repository.cameraIdsStream.first()[1].type)

        assertEquals("5", repository.cameraIdsStream.first()[2].cameraId)
        assertEquals(CameraData.Type.TYPE_PHYSICAL, repository.cameraIdsStream.first()[2].type)

        assertEquals("6", repository.cameraIdsStream.first()[3].cameraId)
        assertEquals(CameraData.Type.TYPE_PHYSICAL, repository.cameraIdsStream.first()[3].type)

        assertEquals(CameraExtensionCharacteristics.EXTENSION_AUTOMATIC, repository.cameraIdsStream.first()[0].extensionsList[0])
        assertEquals(CameraExtensionCharacteristics.EXTENSION_HDR, repository.cameraIdsStream.first()[0].extensionsList[1])
    }

    @Test
    @Config(minSdk = Build.VERSION_CODES.P, maxSdk = Build.VERSION_CODES.R)
    fun refreshCameraIdsTest_minSdk_P_maxSdk_R() = runTest {
        val cameraManager = ApplicationProvider.getApplicationContext<Application>().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraCharacteristics = ShadowCameraCharacteristics.newCameraCharacteristics()
        val shadowCameraCharacteristics = shadowOf(cameraCharacteristics)
        shadowCameraCharacteristics.set(
            CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES,
            intArrayOf(),
        )
        shadowOf(cameraManager).addCamera("0", cameraCharacteristics)

        val repository = CameraIdsRepository(
            CameraIdsSystemDataSource(
                ApplicationProvider.getApplicationContext(),
            ),
        )
        repository.refreshCameraIds()

        assertEquals("0", repository.cameraIdsStream.first()[0].cameraId)
        assertEquals(CameraData.Type.TYPE_NORMAL, repository.cameraIdsStream.first()[0].type)
        assertTrue(repository.cameraIdsStream.first()[0].extensionsList.isEmpty())
    }

    @Test
    @Config(maxSdk = Build.VERSION_CODES.O_MR1)
    fun refreshCameraIdsTest_maxSdk_OMR1() = runTest {
        val cameraManager = ApplicationProvider.getApplicationContext<Application>().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        shadowOf(cameraManager).addCamera("0", ShadowCameraCharacteristics.newCameraCharacteristics())

        val repository = CameraIdsRepository(
            CameraIdsSystemDataSource(
                ApplicationProvider.getApplicationContext(),
            ),
        )
        repository.refreshCameraIds()

        assertEquals("0", repository.cameraIdsStream.first()[0].cameraId)
        assertEquals(CameraData.Type.TYPE_NORMAL, repository.cameraIdsStream.first()[0].type)
        assertTrue(repository.cameraIdsStream.first()[0].extensionsList.isEmpty())
    }
}
