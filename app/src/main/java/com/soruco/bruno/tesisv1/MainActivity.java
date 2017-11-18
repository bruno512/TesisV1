package com.soruco.bruno.tesisv1;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ToggleButton;
import android.hardware.camera2.CameraAccessException;
import android.view.View;
import android.net.Uri;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Vibrator;
import android.content.Context;


public class MainActivity extends AppCompatActivity {

    private CameraManager mCameraManager;
    private String mCameraId=null;
    private ToggleButton mButtonLights;

    private String getCameraId() {
        try {
            String[] ids = mCameraManager.getCameraIdList();
            for (String id : ids) {
                CameraCharacteristics c = mCameraManager.getCameraCharacteristics(id);
                Boolean flashAvailable = c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                Integer facingDirection = c.get(CameraCharacteristics.LENS_FACING);
                if (flashAvailable != null && flashAvailable && facingDirection != null && facingDirection == CameraCharacteristics.LENS_FACING_BACK) {
                    return id;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonLights = (ToggleButton)findViewById(R.id.buttonLights);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mCameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
            mCameraId = getCameraId();
            if (mCameraId==null) {
                mButtonLights.setEnabled(false);
            } else {
                mButtonLights.setEnabled(true);
            }
        } else {
            mButtonLights.setEnabled(false);
        }
    }

    public void clickLights(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mCameraManager.setTorchMode(mCameraId, mButtonLights.isChecked());
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void clickVibrate(View view) {
        // Get instance of Vibrator from current Context
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Start without a delay
        // Vibrate for 100 milliseconds
        // Sleep for 1000 milliseconds
        long[] pattern = {0, 100, 1000};

        // The '0' here means to repeat indefinitely
        // '0' is actually the index at which the pattern keeps repeating from (the start)
        // To repeat the pattern from any other point, you could increase the index, e.g. '1'
        v.vibrate(pattern, 0);
        //((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(10000);
    }

    public void clickStopVibrate(View view) {
        // Get instance of Vibrator from current Context
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.cancel();
    }


    public void clickSound(View view) {
        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), notificationSoundUri);
        ringtone.play();
    }
}

