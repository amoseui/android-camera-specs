package com.amoseui.camerainfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface CameraIdsUiState {
    data object Loading : CameraIdsUiState
    data class Success(val cameraResources: List<CameraResource>) : CameraIdsUiState
}

class CameraIdsViewModel(
    private val cameraIdsRepository: CameraIdsRepository,
): ViewModel() {
    private val data: Flow<List<CameraResource>> = cameraIdsRepository.cameraIdsStream.map {
        it.map { cameraId ->
            CameraResource(
                id = cameraId,
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