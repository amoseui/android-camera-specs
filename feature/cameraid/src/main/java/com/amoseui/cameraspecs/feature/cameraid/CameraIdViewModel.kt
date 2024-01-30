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

package com.amoseui.cameraspecs.feature.cameraid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amoseui.cameraspecs.data.camera2.CameraIdRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CameraIdUiState {
    data object Loading : CameraIdUiState
    data class Success(val cameraIdResources: List<CameraIdResource>) : CameraIdUiState
}

@HiltViewModel
class CameraIdViewModel @Inject constructor(
    private val cameraIdRepository: CameraIdRepository,
) : ViewModel() {
    private val data: Flow<List<CameraIdResource>> = cameraIdRepository.cameraIdStream.map {
        it.map { camera ->
            CameraIdResource(
                id = camera.cameraId,
                type = CameraType.from(camera.type.ordinal)!!,
                extensions = camera.extensionsList.map { extension ->
                    ExtensionType.from(extension)!!
                },
            )
        }
    }

    val uiState: StateFlow<CameraIdUiState> = data.map {
        CameraIdUiState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = CameraIdUiState.Loading,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
    )

    fun getCameraIds() {
        viewModelScope.launch {
            cameraIdRepository.refreshCameraIds()
        }
    }
}
