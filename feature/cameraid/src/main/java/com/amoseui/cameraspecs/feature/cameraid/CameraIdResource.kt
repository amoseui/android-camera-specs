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

enum class CameraType(val value: Int) {
    NORMAL(0),
    LOGICAL(1),
    PHYSICAL(2),
    ;

    companion object {
        fun from(value: Int): CameraType? = entries.find { it.value == value }
    }
}

data class CameraIdResource(
    val id: String,
    val type: CameraType,
)
