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

import android.hardware.camera2.CameraExtensionCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import androidx.annotation.NonNull;
import com.google.common.base.Preconditions;
import java.util.LinkedHashMap;
import java.util.Map;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowCameraManager;

/** Shadow class for {@link CameraManager} */
@Implements(value = CameraManager.class, minSdk = Build.VERSION_CODES.LOLLIPOP)
public class ShadowCameraExtensionManager extends ShadowCameraManager {

  private final Map<String, CameraExtensionCharacteristics> cameraIdToCharacteristics =
      new LinkedHashMap<>();

  public void addCameraExtensionCharacteristics(
      String cameraId, CameraExtensionCharacteristics characteristics) {
    Preconditions.checkNotNull(cameraId);
    Preconditions.checkNotNull(characteristics);
    Preconditions.checkArgument(!cameraIdToCharacteristics.containsKey(cameraId));
    Preconditions.checkNotNull(getCameraCharacteristics(cameraId));

    cameraIdToCharacteristics.put(cameraId, characteristics);
  }

  @Implementation(minSdk = Build.VERSION_CODES.S)
  @NonNull
  protected CameraExtensionCharacteristics getCameraExtensionCharacteristics(
      @NonNull String cameraId) {
    Preconditions.checkNotNull(cameraId);
    CameraExtensionCharacteristics characteristics = cameraIdToCharacteristics.get(cameraId);
    Preconditions.checkArgument(characteristics != null);
    return characteristics;
  }
}
