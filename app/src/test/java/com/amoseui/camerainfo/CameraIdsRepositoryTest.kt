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

package com.amoseui.camerainfo

import android.app.Application
import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowCameraCharacteristics

@HiltAndroidTest
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
@RunWith(RobolectricTestRunner::class)
class CameraIdsRepositoryTest {

    @Test
    fun refreshCameraIdsTest() = runTest {
        val cameraManager = ApplicationProvider.getApplicationContext<Application>().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        shadowOf(cameraManager).addCamera("0", ShadowCameraCharacteristics.newCameraCharacteristics())

        val repository = CameraIdsRepository(CameraIdsSystemDataSource(ApplicationProvider.getApplicationContext()))
        repository.refreshCameraIds()

        assertEquals("0", repository.cameraIdsStream.first()[0].cameraId)
        assertEquals(CameraData.Type.TYPE_NORMAL, repository.cameraIdsStream.first()[0].type)
    }
}
