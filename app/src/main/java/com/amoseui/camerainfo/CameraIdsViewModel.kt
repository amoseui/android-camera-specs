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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CameraIdsUiState {
    data object Loading : CameraIdsUiState
    data class Success(val cameraResources: List<CameraResource>) : CameraIdsUiState
}

@HiltViewModel
class CameraIdsViewModel @Inject constructor(
    private val cameraIdsRepository: CameraIdsRepository,
) : ViewModel() {
    private val data: Flow<List<CameraResource>> = cameraIdsRepository.cameraIdsStream.map {
        it.map { camera ->
            CameraResource(
                id = camera.cameraId,
                type = CameraType.from(camera.type.ordinal)!!,
            )
        }
    }

    val uiState: StateFlow<CameraIdsUiState> = data.map {
        CameraIdsUiState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = CameraIdsUiState.Loading,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
    )
    fun getCameraIds() {
        viewModelScope.launch {
            cameraIdsRepository.refreshCameraIds()
        }
    }
}
