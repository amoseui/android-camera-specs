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

package com.amoseui.cameraspecs.testing.shadows;

import android.hardware.camera2.CameraCharacteristics;
import android.os.Build;
import androidx.annotation.NonNull;
import java.util.HashSet;
import java.util.Set;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowCameraCharacteristics;

@Implements(value = CameraCharacteristics.class, minSdk = Build.VERSION_CODES.LOLLIPOP)
public class ShadowCameraCharacteristicsExtended extends ShadowCameraCharacteristics {

  @Implementation(minSdk = Build.VERSION_CODES.P)
  @NonNull
  public Set<String> getPhysicalCameraIds() {
    Set<String> physicalCameraIds = new HashSet<>();
    physicalCameraIds.add("2");
    physicalCameraIds.add("5");
    physicalCameraIds.add("6");
    return physicalCameraIds;
  }
}
