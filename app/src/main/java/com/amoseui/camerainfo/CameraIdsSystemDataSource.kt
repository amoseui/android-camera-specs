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

import android.content.Context
import android.hardware.camera2.CameraManager
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CameraIdsSystemDataSource(private val dataStore: DataStore<Camera>) {

        val cameraIdsStream: Flow<List<String>> = dataStore.data.map {
                it.cameraIdsList
        }

        suspend fun refreshCameraIds() {
                dataStore.updateData {
                        it.toBuilder()
                                .clearCameraIds()
                                .addAllCameraIds(
//                                        CameraIdsSystem.getCameraIds()
                                        mutableListOf("0", "1", "2", "3")
                                )
                                .build()
                }
        }

        private fun getCameraIds(context: Context): Array<String> {
                val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                return cameraManager.cameraIdList
        }
}
