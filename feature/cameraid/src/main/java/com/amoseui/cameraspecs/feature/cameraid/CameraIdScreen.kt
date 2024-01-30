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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CameraIdScreen(
    cameraIdViewModel: CameraIdViewModel = hiltViewModel(),
    innerPadding: PaddingValues,
) {
    cameraIdViewModel.getCameraIds()
    val uiState by cameraIdViewModel.uiState.collectAsStateWithLifecycle()
    CameraIdScreen(
        uiState = uiState,
        innerPadding = innerPadding,
    )
}

@Composable
private fun CameraIdScreen(
    uiState: CameraIdUiState,
    innerPadding: PaddingValues,
) {
    when (uiState) {
        is CameraIdUiState.Loading -> {
            Text(text = "Loading...")
        }
        is CameraIdUiState.Success -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(innerPadding),
            ) {
                items(uiState.cameraIdResources) { cameraResource ->
                    CameraIdCard(
                        cameraIdResource = cameraResource,
                    )
                }
            }
        }
    }
}

@Composable
private fun CameraIdCard(
    cameraIdResource: CameraIdResource,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .size(width = 200.dp, height = 200.dp),
    ) {
        Text(
            text = "Camera ID ${cameraIdResource.id}",
            modifier = Modifier
                .padding(8.dp),
            textAlign = TextAlign.Center,
        )
        SuggestionChip(
            modifier = Modifier
                .padding(start = 4.dp, bottom = 4.dp),
            label = { Text(cameraIdResource.type.name.lowercase().replaceFirstChar(Char::uppercase)) },
            onClick = { },
        )
        LazyColumn {
            items(cameraIdResource.extensions) {
                SuggestionChip(
                    modifier = Modifier
                        .padding(start = 4.dp, bottom = 4.dp),
                    label = { Text(it.name) },
                    onClick = { },
                )
            }
        }
    }
}
