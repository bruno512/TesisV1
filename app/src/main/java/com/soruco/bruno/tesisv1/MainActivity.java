package com.soruco.bruno.tesisv1;

import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;
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
    AudioManager audioManager;
    MediaPlayer mediaPlayer;
    Button btn_encendido;
    Button btn_apagado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mCameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
            mCameraId = getCameraId();
            if (mCameraId==null) {
                //No hay camara
            } else {
               //Si hay camara
            }
        } else {
            //La version no corresponde
        }

        btn_encendido=(Button)findViewById(R.id.btn_todoe);
        btn_apagado=(Button)findViewById(R.id.btn_todoa);
    }

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

    public void encender(View v)  {
        //Encendemos el flash
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mCameraManager.setTorchMode(mCameraId, true);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
        for (int i=500;i<10000;i+=500){
            apagarflash(i);
            encenderflash(i+500);
        }

       /* apagarflash(1500);
        encenderflash(2000);
        apagarflash(2500);
        encenderflash(3000);
        apagarflash(3500);
        encenderflash(4000);
        apagarflash(4500);
        encenderflash(5000);*/


        //Encendemos el sonido
        mediaPlayer = MediaPlayer.create(getApplication(), R.raw.sonido_humocorto);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        btn_encendido.setEnabled(false);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 3, 0);

        //Encendemos la vibracion
        Vibrator vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 1000, 1000};
        vi.vibrate(pattern, 0);

        //Aumentamos el volumen en 4 seg
        esperarysonar(4000);
    }

    public void esperarysonar(int milisegundos) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,10, 0);
            }
        }, milisegundos);
    }

    public void apagarflash(int milisegundos) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        mCameraManager.setTorchMode(mCameraId, false);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, milisegundos);
    }

    public void encenderflash(int milisegundos) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        mCameraManager.setTorchMode(mCameraId, true);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, milisegundos);
    }



    public void apagar(View view) {
        //Apagamos sonido
        mediaPlayer.stop();
        mediaPlayer.release();

        //Apagamos vibracion
        Vibrator vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vi.cancel();

        //Apagamos flash
        //Encendemos el flash
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mCameraManager.setTorchMode(mCameraId, false);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        btn_encendido.setEnabled(true);
    }
}

