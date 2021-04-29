package org.openbot.utils;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Size;
import org.openbot.original.CameraConnectionFragment;

import timber.log.Timber;

public class CameraResolutionSelector {
    public static class Resolution {
        public Resolution(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int width;
        public int height;
    }

    public Resolution getResolution(Context context, Resolution defaultResolution) {
        final CameraManager manager =
                (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        Resolution resolution = new Resolution(defaultResolution.width, defaultResolution.height);

        try {
            for (final String cameraId : manager.getCameraIdList()) {
                final CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
                final Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != CameraCharacteristics.LENS_FACING_BACK) {
                    continue;
                }
                final StreamConfigurationMap map =
                        characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                Size previewSize =
                        CameraConnectionFragment.chooseOptimalSize(
                                map.getOutputSizes(SurfaceTexture.class), defaultResolution.width, defaultResolution.height);
                resolution.width = previewSize.getWidth();
                resolution.height = previewSize.getHeight();
                Timber.d("Resolution %dx%d", resolution.width, resolution.height);
            }

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        return resolution;
    }
}
