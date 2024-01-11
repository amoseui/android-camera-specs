package com.amoseui.camerainfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun CameraIdsScreen(
    viewModel: CameraIdsViewModel,
) {
    val uiState = viewModel.uiState.collectAsState().value
    CameraIdsScreen(
        uiState = uiState,
    )
}

@Composable
fun CameraIdsScreen(
    uiState: CameraIdsUiState,
) {
    when (uiState) {
        is CameraIdsUiState.Loading -> {
            Text(text = "Loading...")
        }
        is CameraIdsUiState.Success -> {
            CameraIds(
                cameraResources = uiState.cameraResources,
            )
        }
    }
}

@Composable
fun CameraIds(
    cameraResources: List<CameraResource>,
) {
    LazyColumn {
        items(cameraResources) {
            CameraIdCard(
                cameraResource = it,
            )
        }
    }
}

@Composable
fun CameraIdCard(
    cameraResource: CameraResource,
) {
    Column {
        Text(text = cameraResource.id)
    }
}