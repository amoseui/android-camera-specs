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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CameraIdsScreen(
    cameraIdsViewModel: CameraIdsViewModel = viewModel(),
) {
    val uiState by cameraIdsViewModel.uiState.collectAsStateWithLifecycle()
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
