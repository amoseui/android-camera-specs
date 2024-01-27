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

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.cameraDataStore: DataStore<CameraData> by dataStore(
    fileName = "camera.proto",
    serializer = CameraDataSerializer,
)

class CameraIdsSystemDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    val cameraIdsStream: Flow<List<CameraData.Camera>> = context.cameraDataStore.data.map {
        it.camerasList
    }

    suspend fun refreshCameraIds() {
        context.cameraDataStore.updateData {
            it.toBuilder()
                .clearCameras()
                .addAllCameras(
                    getCameraIds(context).map { camera ->
                        CameraData.Camera.newBuilder()
                            .setCameraId(camera.first)
                            .setType(camera.second)
                            .build()
                    },
                )
                .build()
        }
    }

    private fun getCameraIds(context: Context): List<Pair<String, CameraData.Type>> {
        val list = mutableListOf<Pair<String, CameraData.Type>>()
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraManager.cameraIdList.forEach {
            val cameraCharacteristics = cameraManager.getCameraCharacteristics(it)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && cameraCharacteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)
                    ?.contains(CameraMetadata.REQUEST_AVAILABLE_CAPABILITIES_LOGICAL_MULTI_CAMERA) == true
            ) {
                list.add(it to CameraData.Type.TYPE_LOGICAL)
                cameraCharacteristics.physicalCameraIds.forEach { physicalCameraId ->
                    list.add(physicalCameraId to CameraData.Type.TYPE_PHYSICAL)
                }
            } else {
                list.add(it to CameraData.Type.TYPE_NORMAL)
            }
        }
        return list
    }
}
