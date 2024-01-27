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
import android.os.Build.VERSION_CODES;
import androidx.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.List;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.util.ReflectionHelpers;

@Implements(value = CameraExtensionCharacteristics.class, minSdk = VERSION_CODES.S)
public class ShadowCameraExtensionCharacteristics {

  /** Convenience method which returns a new instance of {@link CameraExtensionCharacteristics}. */
  @RequiresApi(api = VERSION_CODES.S)
  public static CameraExtensionCharacteristics newCameraExtensionCharacteristics() {
    return ReflectionHelpers.callConstructor(CameraExtensionCharacteristics.class);
  }

  @Implementation
  public List<Integer> getSupportedResolutions() {
    return new ArrayList<>();
  }
}
